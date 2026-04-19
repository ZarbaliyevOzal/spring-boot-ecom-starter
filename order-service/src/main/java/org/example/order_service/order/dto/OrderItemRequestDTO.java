package org.example.order_service.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderItemRequestDTO {

    @NotNull
    @Positive
    private Long productId;

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotNull
    @Positive
    private BigDecimal unitPrice;
}
