package org.example.order_service.order.messaging.event;

import lombok.*;
import org.example.order_service.order.OrderItem;
import org.example.order_service.order.type.OrderEventType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class OrderEvent {

    private Long orderId;
    private OrderEventType eventType;
    private Long userId;
    private BigDecimal totalPrice;
    private List<OrderItem> orderItems = new ArrayList<>();
    private Instant createdAt;
    private Instant updatedAt;
}
