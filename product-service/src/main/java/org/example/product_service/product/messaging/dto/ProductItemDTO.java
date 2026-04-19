package org.example.product_service.product.messaging.dto;

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
