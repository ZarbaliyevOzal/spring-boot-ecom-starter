package org.example.product_service.product.messaging.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.product_service.product.messaging.event.ProductCommand;
import org.example.product_service.product.messaging.event.ProductCommandEvent;
import org.example.product_service.product.messaging.producer.ProductCommandEventProducer;
import org.example.product_service.product.messaging.type.ProductCommandEventType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductCommandListener {

    private final ObjectMapper objectMapper;
    private final ProductCommandEventProducer producer;

    @KafkaListener(topics = "${app.kafka.topics.product-commands.name}")
    public void handle(ConsumerRecord<String, Object> event) throws JsonProcessingException {

        System.out.println("LISTENER TRIGGERED");
        System.out.println("Topic: " + event.topic());

        ProductCommand productCommand =
                objectMapper.convertValue(event.value(), ProductCommand.class);

        System.out.println("RESULT: " + productCommand);

        // for simplicity produce product reserved event
        producer.send(ProductCommandEvent.builder()
                        .orderId(productCommand.getOrderId())
                        .products(productCommand.getProducts())
                        .totalPrice(productCommand.getTotalPrice())
                        .eventType(ProductCommandEventType.PRODUCT_RESERVED)
                .build());
    }
}
