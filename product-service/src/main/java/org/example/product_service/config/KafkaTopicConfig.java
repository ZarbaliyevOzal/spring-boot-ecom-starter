package org.example.product_service.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public KafkaAdmin.NewTopics allTopics() {
        NewTopic[] newTopics = kafkaProperties.topics().values().stream()
                .map(config -> TopicBuilder.name(config.name())
                        .partitions(config.partition())
                        .replicas(config.replicationFactor())
                        .config("retention.ms", String.valueOf(config.retentionMs()))
                        .config("cleanup.policy", config.cleanupPolicy())
                        .build())
                .toArray(NewTopic[]::new);

        return new KafkaAdmin.NewTopics(newTopics);
    }
}
