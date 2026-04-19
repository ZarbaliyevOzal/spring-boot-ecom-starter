package org.example.order_service.order.messaging.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ProductItemDTO {

    private Long productId;
    private Integer quantity;
    private BigDecimal unitPrice;
}
