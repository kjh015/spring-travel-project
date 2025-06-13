package com.traveler.favorite.kafka;

import com.traveler.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final FavoriteService favoriteService;
    @KafkaListener(topics = "BOARD_TOPIC", groupId = "travel-consumer-favorite")
    public void deleteBoard(String msg) throws IOException {
        try {
            Long boardId = Long.valueOf(msg);
            favoriteService.deleteFavoriteByBoard(boardId);
        } catch (Exception e) {
            System.err.println("Kafka message handling failed: " + e.getMessage());
        }
    }
}
