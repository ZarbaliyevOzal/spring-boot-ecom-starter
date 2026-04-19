package org.example.product_service.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@Validated
@ConfigurationProperties(prefix = "app.kafka")
public record KafkaProperties (
    @Valid
    Map<String, TopicConfig> topics
) {
    public record TopicConfig(
        @NotBlank String name,
        @Min(1) int partition,
        @Min(1) int replicationFactor,
        long retentionMs,
        String cleanupPolicy
    ){}
}
