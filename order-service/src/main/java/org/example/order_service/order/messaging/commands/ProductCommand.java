package org.example.order_service.order.messaging.commands;

import lombok.*;
import org.example.order_service.order.messaging.dto.ProductItemDTO;
import org.example.order_service.order.type.ProductCommandType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ProductCommand {

    private Long orderId;
    private BigDecimal orderTotalPrice;
    private ProductCommandType commandType;
    private List<ProductItemDTO> orderItems = new ArrayList<>();
}
