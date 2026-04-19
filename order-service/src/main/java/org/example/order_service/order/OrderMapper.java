package org.example.order_service.order;

import org.example.order_service.order.dto.OrderItemRequestDTO;
import org.example.order_service.order.dto.OrderItemResponseDTO;
import org.example.order_service.order.dto.OrderRequestDTO;
import org.example.order_service.order.dto.OrderResponseDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {

    public Order toEntity(Long authId, OrderRequestDTO orderRequestDTO) {
        if (orderRequestDTO == null) {
            return null;
        }

        Order order = new Order();
        order.setUserId(authId);

        // Calculate total price and map order items
        BigDecimal totalPrice = BigDecimal.ZERO;

        if (orderRequestDTO.getOrderItems() != null) {
            for (OrderItemRequestDTO itemDTO : orderRequestDTO.getOrderItems()) {
                OrderItem orderItem = toOrderItemEntity(itemDTO);
                order.addOrderItem(orderItem);

                // Accumulate total price
                if (itemDTO.getQuantity() != null && itemDTO.getUnitPrice() != null) {
                    totalPrice = totalPrice.add(
                            BigDecimal.valueOf(itemDTO.getQuantity())
                                    .multiply(itemDTO.getUnitPrice())
                    );
                }
            }
        }

        order.setTotalPrice(totalPrice);

        return order;
    }

    private OrderItem toOrderItemEntity(OrderItemRequestDTO itemDTO) {
        if (itemDTO == null) {
            return null;
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(itemDTO.getProductId());
        orderItem.setQuantity(itemDTO.getQuantity());
        orderItem.setUnitPrice(itemDTO.getUnitPrice());

        return orderItem;
    }

    public OrderResponseDTO toDTO(Order order) {
        List<OrderItemResponseDTO> orderItems = new ArrayList<>();

        order.getOrderItems().forEach(item -> {
                    orderItems.add(
                            OrderItemResponseDTO.builder()
                                    .unitPrice(item.getUnitPrice())
                                    .quantity(item.getQuantity())
                                    .totalPrice(item.getTotalPrice())
                                    .createdAt(item.getCreatedAt())
                                    .updatedAt(item.getUpdatedAt())
                                    .build()
                    );
                });

        return OrderResponseDTO.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .orderItems(orderItems)
                .build();
    }
}
