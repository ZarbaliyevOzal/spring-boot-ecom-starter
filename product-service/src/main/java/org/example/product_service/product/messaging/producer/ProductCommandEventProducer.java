package org.example.product_service.product.messaging.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.product_service.config.KafkaProperties;
import org.example.product_service.product.messaging.event.ProductCommandEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductCommandEventProducer {

    private final KafkaTemplate<String, ProductCommandEvent> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    public void send(ProductCommandEvent event) {
        String topicName = kafkaProperties.topics().get("product-command-events").name();

        // Key = productId → All events for same product go to same partition
        CompletableFuture<SendResult<String, ProductCommandEvent>> future =
                kafkaTemplate.send(topicName, "ORDER#" + event.getOrderId().toString(), event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("✅ Command sent successfully | Topic: {} | Partition: {} | Key: {} | Type: {}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        event.getOrderId(),
                        event.getEventType());
            } else {
                log.error("❌ Failed to send command for order {}: {}", event.getOrderId(), ex.getMessage());
                // TODO: Add retry logic or send to Dead Letter Topic
            }
        });
    }
}
