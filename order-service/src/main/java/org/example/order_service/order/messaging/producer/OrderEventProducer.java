package org.example.order_service.order.messaging.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.order_service.config.KafkaProperties;
import org.example.order_service.order.Order;
import org.example.order_service.order.messaging.event.OrderEvent;
import org.example.order_service.order.type.OrderEventType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    public void send(OrderEvent event) {
        String topicName = kafkaProperties.topics().get("order-events").name();

        // Key = productId → All events for same product go to same partition
        CompletableFuture<SendResult<String, OrderEvent>> future =
                kafkaTemplate.send(topicName, "ORDER#" + event.getOrderId().toString(), event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("✅ Event sent successfully | Topic: {} | Partition: {} | Key: {} | Type: {}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        event.getOrderId(),
                        event.getEventType());
            } else {
                log.error("❌ Failed to send event for product {}: {}", event.getOrderId(), ex.getMessage());
                // TODO: Add retry logic or send to Dead Letter Topic
            }
        });
    }

    // Convenience methods
    public void publishOrderCreated(Order order) {
//        OrderEvent event = OrderEvent.builder()
//                .orderId(orderId)
//                .eventType(OrderEventType.ORDER_CREATED)
//                .price(price)
//                .timestamp(Instant.now())
//                .build();
//        OrderEvent event = new OrderEvent();
//        event.setOrderId(order.getId());
//        event.setUserId(order.getUserId());
//        event.setTotalPrice(order.getTotalPrice());
//        event.setCreatedAt(order.getCreatedAt());
//        event.setOrderItems(order.getOrderItems());

        send(OrderEvent.builder()
                .orderId(order.getId())
                .eventType(OrderEventType.ORDER_CREATED)
                .userId(order.getUserId())
                .totalPrice(order.getTotalPrice())
                .orderItems(order.getOrderItems())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build());
    }
}
