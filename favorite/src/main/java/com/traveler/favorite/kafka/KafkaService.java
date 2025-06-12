package com.traveler.favorite.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class KafkaService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaTemplate<String,String> kafkaTemplate;

    public void updateFavoriteCount(Long boardId, boolean isAdd){
        try {
            Map<String, String> newRating = new HashMap<>();
            newRating.put("boardId", String.valueOf(boardId));
            newRating.put("isAdd", String.valueOf(isAdd));
            this.kafkaTemplate.send("FAVORITE_TOPIC", objectMapper.writeValueAsString(newRating));
        } catch (JsonProcessingException e) {
            System.err.println("Kafka message handling failed: " + e.getMessage());
        }
    }







}
