package org.example.product_service.product.messaging.event;

import lombok.*;
import org.example.product_service.product.messaging.dto.ProductItemDTO;
import org.example.product_service.product.messaging.type.ProductCommandType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ProductCommand {

    private Long orderId;
    private ProductCommandType eventType;
    private BigDecimal totalPrice;
    private List<ProductItemDTO> products = new ArrayList<>();
    private String message;
    private List<Map<String, Object>> errors = new ArrayList<>();
}
