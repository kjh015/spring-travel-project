package com.traveler.board.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final BoardService boardService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "COMMENT_TOPIC", groupId = "travel-consumer-board")
    public void updateRatingAvg(ConsumerRecord<String, String> record) throws IOException {
        try {
            Map<String, String> newRating = objectMapper.readValue(record.value(), new TypeReference<>() {});
            Long boardId = Long.valueOf(newRating.get("boardId"));
            Integer rating = Integer.valueOf(newRating.get("rating"));
            boolean isAdd = Boolean.parseBoolean(newRating.get("isAdd"));
            boardService.updateRatingAvg(boardId, rating, isAdd);
        } catch (Exception e) {
            System.err.println("Kafka message handling failed: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "FAVORITE_TOPIC", groupId = "travel-consumer-board")
    public void updateFavoriteCount(ConsumerRecord<String, String> record) throws IOException {
        try {
            Map<String, String> newFavorite = objectMapper.readValue(record.value(), new TypeReference<>() {});
            Long boardId = Long.valueOf(newFavorite.get("boardId"));
            boolean isAdd = Boolean.parseBoolean(newFavorite.get("isAdd"));
            boardService.updateFavoriteCount(boardId, isAdd);
        } catch (Exception e) {
            System.err.println("Kafka message handling failed: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "VIEWCOUNT_TOPIC", groupId = "travel-consumer-board")
    public void updateViewCount(String msg) throws IOException {
        Long boardId = Long.valueOf(msg);
        boardService.updateViewCount(boardId);
    }
}