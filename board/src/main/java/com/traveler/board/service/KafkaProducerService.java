package com.traveler.board.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KafkaProducerService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaTemplate<String,String> kafkaTemplate;

    public void deleteBoard(Long boardId){
        kafkaTemplate.send("BOARD_TOPIC", String.valueOf(boardId));
    }

}
