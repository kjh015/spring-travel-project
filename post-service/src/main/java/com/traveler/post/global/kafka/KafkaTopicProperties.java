package com.traveler.post.global.kafka;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.kafka.topics")
public record KafkaTopicProperties(Map<String, String> mapping) {
    public KafkaTopicProperties {
        mapping = (mapping == null) ? Map.of() : Map.copyOf(mapping);
    }

    public String getTopic(String key) {
        return mapping.getOrDefault(key, "default-topic");
    }
}
