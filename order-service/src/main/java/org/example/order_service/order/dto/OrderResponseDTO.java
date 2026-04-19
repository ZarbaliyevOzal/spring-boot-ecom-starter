package org.example.order_service.order.dto;

import lombok.*;
import org.example.order_service.order.type.OrderStatus;

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
public class OrderResponseDTO {

    private Long id;
    private Long userId;
    private BigDecimal totalPrice;
    private List<OrderItemResponseDTO> orderItems = new ArrayList<>();
    private OrderStatus status;
    private Instant createdAt;
}
