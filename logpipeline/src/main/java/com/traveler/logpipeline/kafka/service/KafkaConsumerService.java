package com.traveler.logpipeline.kafka.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.logpipeline.entity.Filter;
import com.traveler.logpipeline.entity.Format;
import com.traveler.logpipeline.entity.LogFail;
import com.traveler.logpipeline.entity.LogSuccess;
import com.traveler.logpipeline.kafka.dto.LogDto;
import com.traveler.logpipeline.service.FilterService;
import com.traveler.logpipeline.service.FormatService;
import com.traveler.logpipeline.service.LogFailService;
import com.traveler.logpipeline.service.LogSuccessService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Component
public class KafkaConsumerService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final FormatService formatService;
    private final FilterService filterService;
    private final LogSuccessService logSuccessService;
    private final LogFailService logFailService;
    private final KafkaTemplate<String,String> kafkaTemplate;

    public KafkaConsumerService(FormatService formatService, FilterService filterService, LogSuccessService logSuccessService, LogFailService logFailService, KafkaTemplate<String, String> kafkaTemplate) {
        this.formatService = formatService;
        this.filterService = filterService;
        this.logSuccessService = logSuccessService;
        this.logFailService = logFailService;
        this.kafkaTemplate = kafkaTemplate;
    }

    static Long processId = 1L;

    @KafkaListener(topics = "START_TOPIC", groupId = "matomo-log-consumer")
    public void startTopic(ConsumerRecord<String, String> record) {
        try {
            LogDto log = objectMapper.readValue(record.value(), LogDto.class);
            System.out.println("Start Topic Consumed: " + log.toString());
            //log에서 query 부분 추출
            Map<String, String> query = parseQuery(log.getQuery());

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
            items.putAll(query);
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
            }
            else{
                items.put("success", "false");
                items.put("failBy", failBy.toString());
            }
            kafkaTemplate.send("DB_TOPIC", objectMapper.writeValueAsString(items));
        } catch (Exception e) {
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
                LogFail log = new LogFail();
                log.setLogJson(objectMapper.writeValueAsString(items));
                logFailService.addFailLog(log, processId, Long.parseLong(items.get("failBy")));
            }
        } catch (Exception e) {
            System.err.println("Kafka message handling failed: " + e.getMessage());
        }
        
    }

    public Map<String, String> parseQuery(String query) {
        String fakeUrl = "http://dummy?" + query;
        return UriComponentsBuilder.fromUriString(fakeUrl)
                .build()
                .getQueryParams()
                .toSingleValueMap();
    }

}
