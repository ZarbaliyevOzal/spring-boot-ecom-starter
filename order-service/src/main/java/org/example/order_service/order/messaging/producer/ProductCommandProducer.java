package org.example.order_service.order.messaging.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.order_service.config.KafkaProperties;
import org.example.order_service.order.Order;
import org.example.order_service.order.messaging.commands.ProductCommand;
import org.example.order_service.order.messaging.dto.ProductItemDTO;
import org.example.order_service.order.type.ProductCommandType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductCommandProducer {

    private final KafkaTemplate<String, ProductCommand> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    public void send(ProductCommand command) {
        String topicName = kafkaProperties.topics().get("product-commands").name();

        // Key = productId → All events for same product go to same partition
        CompletableFuture<SendResult<String, ProductCommand>> future =
                kafkaTemplate.send(topicName, "ORDER#" + command.getOrderId().toString(), command);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("✅ Command sent successfully | Topic: {} | Partition: {} | Key: {} | Type: {}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        command.getOrderId(),
                        command.getCommandType());
            } else {
                log.error("❌ Failed to send command for order {}: {}", command.getOrderId(), ex.getMessage());
                // TODO: Add retry logic or send to Dead Letter Topic
            }
        });
    }

    public void sendReserveProductCommand(Order order) {
        List<ProductItemDTO> orderItems = new ArrayList<>();
        order.getOrderItems()
                .forEach(item -> {
                    orderItems.add(ProductItemDTO.builder()
                            .productId(item.getProductId())
                            .unitPrice(item.getUnitPrice())
                            .quantity(item.getQuantity())
                            .build());
                });

        send(ProductCommand.builder()
                .orderId(order.getId())
                .commandType(ProductCommandType.RESERVE_PRODUCT)
                .orderTotalPrice(order.getTotalPrice())
                .orderItems(orderItems)
                .build());
    }

    public void sendReleaseProductCommand(Order order) {
        List<ProductItemDTO> orderItems = new ArrayList<>();
        order.getOrderItems()
                .forEach(item -> {
                    orderItems.add(ProductItemDTO.builder()
                            .productId(item.getProductId())
                            .unitPrice(item.getUnitPrice())
                            .quantity(item.getQuantity())
                            .build());
                });

        send(ProductCommand.builder()
                .orderId(order.getId())
                .commandType(ProductCommandType.RELEASE_PRODUCT)
                .orderTotalPrice(order.getTotalPrice())
                .orderItems(orderItems)
                .build());
    }
}
