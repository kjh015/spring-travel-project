package com.traveler.post.global.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "kafka.topics")
public record KafkaTopicProperties(Map<String, String> mapping) {
    public String getTopic(String key) {
        return mapping.getOrDefault(key, "default-topic");
    }
}
