package org.example.order_service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.order_service.order.messaging.commands.NotificationCommand;
import org.example.order_service.order.messaging.commands.PaymentCommand;
import org.example.order_service.order.messaging.event.PaymentCommandEvent;
import org.example.order_service.order.messaging.event.ProductCommandEvent;
import org.example.order_service.order.messaging.producer.NotificationCommandProducer;
import org.example.order_service.order.messaging.producer.PaymentCommandProducer;
import org.example.order_service.order.messaging.producer.ProductCommandProducer;
import org.example.order_service.order.type.OrderEventType;
import org.example.order_service.order.type.OrderStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderSagaOrchestrator {

    private final ProductCommandProducer productCommandProducer;
    private final OrderRepository orderRepository;
    private final OrderSagaStateRepository orderSagaStateRepository;
    private final PaymentCommandProducer paymentCommandProducer;
    private final ObjectMapper objectMapper;
    private final NotificationCommandProducer notificationCommandProducer;

    private Order findOrder(Long id) {
        // find order
        Optional<Order> optionalOrder = orderRepository.findByDeletedAtIsNullAndId(id);

        if (optionalOrder.isEmpty()) {
            log.error("Order saga orchestrator: order with id {} not found", id);
            // todo: produce release products command
            return null;
        }

        return optionalOrder.get();
    }

    public void onOrderCreated(Order order) {
        if (order.getStatus() != OrderStatus.CREATED) return;

        // produce reserve product command
        productCommandProducer.sendReserveProductCommand(order);
    }

    public void onProductReserved(ProductCommandEvent event) {
        // find order
        Order order = findOrder(event.getOrderId());

        if (order == null) return;

        // check order status is "CREATED"
        // if order status not "CREATED" then ignore
        if (order.getStatus() != OrderStatus.CREATED) {
            return;
        }

        // create order saga state log
        orderSagaStateRepository.save(
                OrderSagaState.builder()
                        .order(order)
                        .eventType(OrderEventType.PRODUCT_RESERVED)
                        .build()
        );

        order.setStatus(OrderStatus.PRODUCT_RESERVED);
        orderRepository.save(order);

        // produce process payment command
        paymentCommandProducer.send(
                PaymentCommand.builder()
                        .orderId(order.getId())
                        .orderTotalPrice(order.getTotalPrice())
                        .createdAt(Instant.now())
                .build()
        );
    }

    public void onProductFailed(ProductCommandEvent event) throws JsonProcessingException {
        // find order
        Order order = findOrder(event.getOrderId());

        if (order == null) return;

        // check order status is "CREATED"
        // if order status not "CREATED" then ignore
        if (order.getStatus() != OrderStatus.CREATED) return;

        String errors = !event.getErrors().isEmpty()
                ? objectMapper.writeValueAsString(event.getErrors())
                : null;

        // create order saga state log
        orderSagaStateRepository.save(OrderSagaState.builder()
                        .order(order)
                        .eventType(OrderEventType.PRODUCT_FAILED)
                        .reason(errors)
                .build());

        // cancel order
        order.setStatus(OrderStatus.CANCELLED);

        // save order
        orderRepository.save(order);
    }

    public void onProductReleased(ProductCommandEvent event) {
        // find order
        Order order = findOrder(event.getOrderId());

        if (order == null) return;

        // create order saga state log
        orderSagaStateRepository.save(OrderSagaState.builder()
                .order(order)
                .eventType(OrderEventType.PRODUCT_RELEASED)
                .build());
    }

    public void onPaymentSuccess(PaymentCommandEvent event) {
        // find order
        Order order = findOrder(event.getOrderId());

        if (order == null) return;

        // if order status is not equal to "product reserved", then ignore
        if (order.getStatus() != OrderStatus.PRODUCT_RESERVED) return;

        // create order saga state log
        orderSagaStateRepository.save(OrderSagaState.builder()
                .order(order)
                .eventType(OrderEventType.PAYMENT_PROCESSED)
                .build());

        // update order status
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        // produce notification command
        notificationCommandProducer.send(NotificationCommand.builder()
                        .orderId(order.getId())
                        .userId(order.getUserId())
                        .createdAt(Instant.now())
                .build());
    }

    public void onPaymentFail(PaymentCommandEvent event) {
        // find order
        Order order = findOrder(event.getOrderId());

        if (order == null) return;

        // if order status is not equal to "product reserved", then ignore
        if (order.getStatus() != OrderStatus.PRODUCT_RESERVED) return;

        // create order saga state log
        orderSagaStateRepository.save(
                OrderSagaState.builder()
                    .order(order)
                    .eventType(OrderEventType.PAYMENT_FAILED)
                    .reason(event.getMessage())
                .build()
        );

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        // produce release product command
        productCommandProducer.sendReleaseProductCommand(order);
    }
}
