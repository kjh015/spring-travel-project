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

import java.util.Map;

@Component
public class KafkaConsumerService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final FormatRepository formatRepository;
    private final KafkaTemplate<String,Object> kafkaTemplate;

    public KafkaConsumerService(FormatRepository formatRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.formatRepository = formatRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "START_TOPIC", groupId = "matomo-log-consumer")
    public void startTopic(ConsumerRecord<String, String> record) {
        Long formatId = 1L;

        try {
            LogDto log = objectMapper.readValue(record.value(), LogDto.class);
            System.out.println("Start Topic Consumed: " + log.toString());

            //DB에서 format 정보 꺼냄
            Format entity = formatRepository.findById(formatId).orElse(null);
            if (entity == null) return;

            //꺼낸 정보에서 format 부분만 추출
            Map<String, String> format = objectMapper.readValue(entity.getFormatJson(), new TypeReference<>() {});

            if (format != null) {
                //Kafka에서 받아온 데이터에 DB에서 꺼낸 format 추가
                log.getAdditionalData().putAll(format);
            }

            //send
            kafkaTemplate.send("FILTER_TOPIC", objectMapper.writeValueAsString(log));
        } catch (Exception e) {
            System.err.println("Kafka message handling failed: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "FILTER_TOPIC", groupId = "matomo-log-consumer")
    public void filterTopic(ConsumerRecord<String, String> record){
        try {
            String json = record.value();
            System.out.println("Filter Topic Consumed: " + json);
        } catch (Exception e) {
            System.err.println("Kafka message handling failed: " + e.getMessage());
        }
    }

    public void handleKafkaMessage(String processId, String formatId, String json, String targetTopic){

    }

}
