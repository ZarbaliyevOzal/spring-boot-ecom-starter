package org.example.order_service.order.messaging.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.order_service.order.OrderSagaOrchestrator;
import org.example.order_service.order.messaging.event.ProductCommandEvent;
import org.example.order_service.order.type.ProductCommandEventType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductCommandEventsListener {

    private final ObjectMapper objectMapper;
    private final OrderSagaOrchestrator orderSagaOrchestrator;

    @KafkaListener(topics = "${app.kafka.topics.product-command-events.name}")
    public void handle(ConsumerRecord<String, Object> event) throws JsonProcessingException {

        System.out.println("LISTENER TRIGGERED");
        System.out.println("Topic: " + event.topic());

        ProductCommandEvent productCommandEvent =
                objectMapper.convertValue(event.value(), ProductCommandEvent.class);

        System.out.println("RESULT: " + productCommandEvent);

        if (productCommandEvent.getEventType() == ProductCommandEventType.PRODUCT_RESERVED) {
            orderSagaOrchestrator.onProductReserved(productCommandEvent);
        } else if (productCommandEvent.getEventType() == ProductCommandEventType.RESERVE_PRODUCT_FAILED) {
            orderSagaOrchestrator.onProductFailed(productCommandEvent);
        } else if (productCommandEvent.getEventType() == ProductCommandEventType.PRODUCT_RELEASED) {
            orderSagaOrchestrator.onProductReleased(productCommandEvent);
        }
    }
}
