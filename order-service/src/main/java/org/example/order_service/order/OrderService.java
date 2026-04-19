package org.example.order_service.order;

import lombok.RequiredArgsConstructor;
import org.example.order_service.order.dto.OrderRequestDTO;
import org.example.order_service.order.dto.OrderResponseDTO;
import org.example.order_service.order.type.OrderEventType;
import org.example.order_service.order.messaging.producer.OrderEventProducer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderEventProducer orderEventProducer;
    private final OrderSagaOrchestrator orderSagaOrchestrator;

    @Transactional
    public OrderResponseDTO createOrder(Long authId, OrderRequestDTO orderRequestDTO) {
        Order order = orderMapper.toEntity(authId, orderRequestDTO);

        OrderSagaState orderSagaState = OrderSagaState.builder()
                .eventType(OrderEventType.ORDER_CREATED)
                .order(order)
                .build();

        order.setOrderSagaStates(List.of(orderSagaState));

        // save order items
        orderRepository.save(order);

        // publish order created event
        orderEventProducer.publishOrderCreated(order);

        // start order saga
        orderSagaOrchestrator.onOrderCreated(order);

        return orderMapper.toDTO(order);
    }
}
