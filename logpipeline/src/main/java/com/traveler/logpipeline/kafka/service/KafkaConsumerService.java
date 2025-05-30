package com.traveler.logpipeline.kafka.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.logpipeline.entity.*;
import com.traveler.logpipeline.kafka.dto.LogDto;
import com.traveler.logpipeline.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

@Component
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final FormatService formatService;
    private final FilterService filterService;
    private final LogSuccessService logSuccessService;
    private final LogFailService logFailService;
    private final KafkaTemplate<String,String> kafkaTemplate;
    private final DeduplicationService deduplicationService;
    private final LogFailByDeduplicationService logFailByDeduplicationService;


    static Long processId = 1L;

//    @KafkaListener(topics = "START_TOPIC", groupId = "matomo-log-consumer")
    public void startTopic2(ConsumerRecord<String, String> record) {
        System.out.println("Start Topic Consumed: " + record.value());
    }


    @KafkaListener(topics = "START_TOPIC", groupId = "matomo-log-consumer")
    public void startTopic(ConsumerRecord<String, String> record) {
        try {
            LogDto log = objectMapper.readValue(record.value(), LogDto.class);
            System.out.println("Start Topic Consumed: " + log.toString());
            //log에서 query 부분 추출
            Map<String, String> query = parseQueryParams(log.getPath());

            System.out.println(query);

            //send할 Map
            Map<String, String> items = new HashMap<>();

            //활성화 된 format만 추출
            List<Format> formats = formatService.activeFormats(processId);
            //로그의 데이터를 포매팅
            for(Format format : formats){
                Map<String, String> formatInfo = objectMapper.readValue(format.getFormatJson(), new TypeReference<>() {});
                Map<String, String> defaultInfo = objectMapper.readValue(format.getDefaultJson(), new TypeReference<>() {});
                if (formatInfo == null || defaultInfo == null) return;
                //DB: 바꿀이름-로그이름 / Log: 로그이름-값
                formatInfo.forEach((key, value) -> {
                    items.put(key, query.getOrDefault(value, null));
                    query.remove(value);
                });
                items.putAll(defaultInfo);
            }
            //send
            kafkaTemplate.send("FILTER_TOPIC", objectMapper.writeValueAsString(items));
        } catch (Exception e) {
            System.err.println("Kafka message handling failed: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "FILTER_TOPIC", groupId = "matomo-log-consumer")
    public void filterTopic(ConsumerRecord<String, String> record){
        System.out.println("Consumed Filter Topic: " + record.value());
        try {
            Map<String, String> items = objectMapper.readValue(record.value(), new TypeReference<>() {});
            List<Filter> filters = filterService.activeFilters(processId);

            boolean success = true;
            Long failBy = 0L;


            //아이템이 활성화된 필터를 모두 거치도록
            for(Filter filter : filters){
                LinkedHashMap<String, String> fields = objectMapper.readValue(filter.getUsedField(), new TypeReference<>() {});

                List<Class<?>> paramTypes = new ArrayList<>();
                List<Object> paramValues = new ArrayList<>();

                fields.forEach((field, type) -> {
                    String value = items.get(field);

                    if (value != null && type != null) {
                        switch (type.toLowerCase()) {
                            case "int" -> {
                                paramTypes.add(int.class);
                                paramValues.add(Integer.parseInt(value));
                            }
                            case "double" -> {
                                paramTypes.add(double.class);
                                paramValues.add(Double.parseDouble(value));
                            }
                            case "string" -> {
                                paramTypes.add(String.class);
                                paramValues.add(value);
                            }
                            case "boolean" -> {
                                paramTypes.add(boolean.class);
                                paramValues.add(Boolean.parseBoolean(value));
                            }
                            default -> throw new IllegalArgumentException("지원하지 않는 타입: " + type);
                        }
                    }
                });
                String fullCode = filter.getSourceCode();

                if(!JavaMethodExecutor.compileAndRunMethod(fullCode, paramTypes.toArray(Class[]::new), paramValues.toArray())){
                    success = false;
                    failBy = filter.getId();
                    break;
                }
            }
            if(success){
                items.put("success", "true");
                kafkaTemplate.send("DEDUPLICATION_TOPIC", objectMapper.writeValueAsString(items));

            }
            else{
                items.put("success", "false");
                items.put("failBy", "filter");
                items.put("failID", failBy.toString());
                kafkaTemplate.send("DB_TOPIC", objectMapper.writeValueAsString(items));
            }
        } catch (Exception e) {
            System.err.println("Kafka message handling failed: " + e.getMessage());
        }
    }
    @KafkaListener(topics = "DEDUPLICATION_TOPIC", groupId = "matomo-log-consumer")
    public void deduplicationTopic(ConsumerRecord<String, String> record){
        System.out.println("Consumed Deduplication Topic: " + record.value());
        try{
            Map<String, String> items = objectMapper.readValue(record.value(), new TypeReference<>() {});
            String userId = items.get("user_id");
            List<Deduplication> ddps = deduplicationService.getActiveDeduplication(processId);

            boolean success = true;

            for(Deduplication ddp : ddps){
                List<Map<String, String>> settings = objectMapper.readValue(ddp.getDeduplicationJson(), new TypeReference<>() {});
                for(Map<String, String> setting : settings){
                    if(items.get(setting.get("format")).equals(setting.get("value"))){
                        Optional<LogPassHistory> _history = deduplicationService.getPassHistory(ddp.getId(), userId);
                        if(_history.isPresent()){
                            LogPassHistory history = _history.get();
                            if(LocalDateTime.now().isAfter(history.getExpiredTime())){  //현재 시간이 만료 시간보다 지났다면
                                //새로 기록하고 통과
                                deduplicationService.updatePassHistory(history, createExpiredTime(setting));
                            }
                            else{
                                //실패 테이블에 저장
                                items.put("success", "false");
                                items.put("failBy", "deduplication");
                                items.put("failID", String.valueOf(ddp.getId()));
                                success = false;
                                break;
                            }
                        }
                        else{
                            deduplicationService.addPassHistory(ddp, processId, createExpiredTime(setting), userId);
                        }
                    }
                }
                if(!success) break;
            }
            kafkaTemplate.send("DB_TOPIC", objectMapper.writeValueAsString(items));

        }catch (Exception e) {
            System.err.println("Kafka message handling failed: " + e.getMessage());
        }

    }

    
    @KafkaListener(topics = "DB_TOPIC", groupId = "matomo-log-consumer")
    public void dbTopic(ConsumerRecord<String, String> record){
        System.out.println("Consumed DB Topic: " + record.value());
        try {
            Map<String, String> items = objectMapper.readValue(record.value(), new TypeReference<>() {});
            //DB 저장
            if(items.get("success").equalsIgnoreCase("true")){
                LogSuccess log = new LogSuccess();
                log.setLogJson(objectMapper.writeValueAsString(items));
                logSuccessService.addSuccessLog(log, processId);
            }
            else{
                if(items.get("failBy").equalsIgnoreCase("filter")){
                    LogFail log = new LogFail();
                    log.setLogJson(objectMapper.writeValueAsString(items));
                    logFailService.addFailLog(log, processId, Long.parseLong(items.get("failID")));
                }
                else{
                    LogFailByDeduplication log = new LogFailByDeduplication();
                    log.setLogJson(objectMapper.writeValueAsString(items));
                    logFailByDeduplicationService.addFailLog(log, processId, Long.parseLong(items.get("failID")));
                }

            }
        } catch (Exception e) {
            System.err.println("Kafka message handling failed: " + e.getMessage());
        }
        
    }

    public Map<String, String> parseQueryParams(String pathWithQuery) {
        Map<String, String> queryPairs = new HashMap<>();
        if (!pathWithQuery.contains("?")) return queryPairs;

        String query = pathWithQuery.substring(pathWithQuery.indexOf('?') + 1);
        String[] pairs = query.split("&");

        for (String pair : pairs) {
            int idx = pair.indexOf('=');
            if (idx == -1) continue;
            String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
            String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
            queryPairs.put(key, value);
        }

        return queryPairs;
    }

    public LocalDateTime createExpiredTime(Map<String, String> setting){
        LocalDateTime now = LocalDateTime.now();
        Period period = Period.of(
                Integer.parseInt(setting.get("year")),
                Integer.parseInt(setting.get("month")),
                Integer.parseInt(setting.get("day"))
        );
        Duration duration = Duration.ofHours(Integer.parseInt(setting.get("hour")))
                .plusMinutes(Integer.parseInt(setting.get("minute")))
                .plusSeconds(Integer.parseInt(setting.get("second")));

        LocalDateTime expiresTime = now.plus(period).plus(duration);
        return expiresTime;
    }

}
