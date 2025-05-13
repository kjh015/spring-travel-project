package com.traveler.logpipeline.kafka.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.logpipeline.entity.Format;
import com.traveler.logpipeline.kafka.dto.LogDto;
import com.traveler.logpipeline.repository.FormatRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class KafkaConsumerService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final FormatRepository formatRepository;
    private final KafkaTemplate<String,String> kafkaTemplate;

    public KafkaConsumerService(FormatRepository formatRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.formatRepository = formatRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "START_TOPIC", groupId = "matomo-log-consumer")
    public void startTopic(ConsumerRecord<String, String> record) {
        try {
            LogDto log = objectMapper.readValue(record.value(), LogDto.class);
            System.out.println("Start Topic Consumed: " + log.toString());
            //log에서 query 부분 추출
            Map<String, String> query = parseQuery(log.getQuery());

            //send할 Map<>
            Map<String, String> items = new HashMap<>();

            //활성화 된 format만 추출
            List<Format> entities = formatRepository.findAllByIsActiveTrue();
            //로그의 데이터를 포매팅
            for(Format entity : entities){
                Map<String, String> formatInfo = objectMapper.readValue(entity.getFormatJson(), new TypeReference<>() {});
                Map<String, String> defaultInfo = objectMapper.readValue(entity.getDefaultJson(), new TypeReference<>() {});
                if (formatInfo == null || defaultInfo == null) return;
                //DB: 바꿀이름-로그이름 / Log: 로그이름-값
                formatInfo.forEach((key, value) -> {
                    items.put(key, query.getOrDefault(value, "none"));
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
        try {
            System.out.println("Consumed Filter Topic: " + record.value());
            Map<String, String> items = objectMapper.readValue(record.value(), new TypeReference<>() {});
            System.out.println(items);
            //필터링 로직
            if(Integer.parseInt(items.get("item_price")) > 20000){
                kafkaTemplate.send("DB_TOPIC", objectMapper.writeValueAsString(items));
            }
        } catch (Exception e) {
            System.err.println("Kafka message handling failed: " + e.getMessage());
        }
    }
    
    @KafkaListener(topics = "DB_TOPIC", groupId = "matomo-log-consumer")
    public void dbTopic(ConsumerRecord<String, String> record){
        try {
            System.out.println("Consumed DB Topic: " + record.value());
            Map<String, String> items = objectMapper.readValue(record.value(), new TypeReference<>() {});
            //DB 저장
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
