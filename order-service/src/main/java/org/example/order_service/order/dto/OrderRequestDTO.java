package org.example.order_service.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderRequestDTO {

    @NotNull
    @Positive
    private Long userId;

    @Valid
    @NotNull
    @Size(min = 1)
    private List<OrderItemRequestDTO> orderItems = new ArrayList<>();
}
