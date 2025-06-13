package com.traveler.comment.kafka.service;

import com.traveler.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final CommentService commentService;
    @KafkaListener(topics = "BOARD_TOPIC", groupId = "travel-consumer-comment")
    public void deleteBoard(String msg) throws IOException {
        try {
            Long boardId = Long.valueOf(msg);
            commentService.deleteCommentByBoard(boardId);
        } catch (Exception e) {
            System.err.println("Kafka message handling failed: " + e.getMessage());
        }
    }
}
