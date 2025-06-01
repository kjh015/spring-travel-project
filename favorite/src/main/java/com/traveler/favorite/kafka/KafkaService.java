package com.traveler.favorite.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaTemplate<String,String> kafkaTemplate;
    private final FavoriteService favoriteService;



//    @KafkaListener(topics = "SIGN_NICKNAME_TOPIC", groupId = "travel-consumer-favorite")
//    public void nicknameTopic(ConsumerRecord<String, String> record) {
//        System.out.println("Consumed: " + record.value());
//        try {
//            Map<String, String> nickname = objectMapper.readValue(record.value(), new TypeReference<>() {});
//            favoriteService.updateNickname(nickname.get("prev"), nickname.get("cur"));
//            System.out.println("complete");
//        } catch (Exception e) {
//            System.err.println("Kafka message handling failed: " + e.getMessage());
//        }
//    }




}
