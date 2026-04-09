package com.traveler.post.global.kafka;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.kafka")
public record KafkaTopicProperties(Map<String, String> topics) {
    public KafkaTopicProperties {
        topics = (topics == null) ? Map.of() : Map.copyOf(topics);
    }

    public String getTopic(String key) {
        return topics.getOrDefault(key, "default-topic");
    }
}
