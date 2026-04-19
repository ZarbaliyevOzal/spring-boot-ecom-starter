package org.example.order_service.order.messaging.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.order_service.order.OrderSagaOrchestrator;
import org.example.order_service.order.messaging.event.PaymentCommandEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentCommandEventsListener {

    private final ObjectMapper objectMapper;
    private final OrderSagaOrchestrator orderSagaOrchestrator;

    @KafkaListener(topics = "${app.kafka.topics.payment-command-events.name}")
    public void handle(ConsumerRecord<String, Object> event) throws JsonProcessingException {

        System.out.println("LISTENER TRIGGERED");
        System.out.println("Topic: " + event.topic());

        PaymentCommandEvent paymentCommandEvent =
                objectMapper.convertValue(event.value(), PaymentCommandEvent.class);

        System.out.println("RESULT: " + paymentCommandEvent);

        if (paymentCommandEvent.getSuccess()) {
            orderSagaOrchestrator.onPaymentSuccess(paymentCommandEvent);
        } else {
            orderSagaOrchestrator.onPaymentFail(paymentCommandEvent);
        }
    }
}
