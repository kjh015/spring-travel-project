package com.traveler.logpipeline.kafka.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class KafkaProducerService {
    /* Kafka Template 을 이용해 Kafka Broker 전송 */

    private final KafkaTemplate<String,String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendMessageToKafka(String topicName, Map<String, String> message) {
//        System.out.printf("Producer Message : %s%n",message);
//        this.kafkaTemplate.send(topicName,message);
    }
}