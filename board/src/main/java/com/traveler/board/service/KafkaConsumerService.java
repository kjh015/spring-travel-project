package com.traveler.board.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class KafkaConsumerService {

//    @KafkaListener(topics = "my-topic", groupId = "consumer_group01")
    public void consume(String message) throws IOException {
        System.out.printf("Consumed Message : %s%n", String.valueOf(message));
    }
}