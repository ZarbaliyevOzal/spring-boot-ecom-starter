package org.example.order_service.order.messaging.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.order_service.config.KafkaProperties;
import org.example.order_service.order.messaging.commands.PaymentCommand;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentCommandProducer {

    private final KafkaTemplate<String, PaymentCommand> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    public void send(PaymentCommand command) {
        String topicName = kafkaProperties.topics().get("payment-commands").name();

        CompletableFuture<SendResult<String, PaymentCommand>> future =
                kafkaTemplate.send(topicName, "ORDER#" + command.getOrderId().toString(), command);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("✅ Command sent successfully | Topic: {} | Partition: {} | Key: {}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        command.getOrderId());
            } else {
                log.error("❌ Failed to send command for order {}: {}", command.getOrderId(), ex.getMessage());
                // TODO: Add retry logic or send to Dead Letter Topic
            }
        });
    }
}
