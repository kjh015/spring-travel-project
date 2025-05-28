package com.traveler.sign.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class KafkaProducerService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaTemplate<String,String> kafkaTemplate;

    public void updateNickname(String prev, String cur){
        Map<String, String> nickname = new HashMap<>();
        nickname.put("prev", prev);
        nickname.put("cur", cur);
        try {
            this.kafkaTemplate.send("SIGN_NICKNAME_TOPIC", objectMapper.writeValueAsString(nickname));
        } catch (JsonProcessingException e) {
            System.err.println("Kafka message handling failed: " + e.getMessage());
        }
    }
}