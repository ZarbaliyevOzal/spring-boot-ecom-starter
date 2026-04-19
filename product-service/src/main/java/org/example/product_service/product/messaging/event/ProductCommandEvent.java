package org.example.product_service.product.messaging.event;

import lombok.*;
import org.example.product_service.product.messaging.dto.ProductItemDTO;
import org.example.product_service.product.messaging.type.ProductCommandEventType;

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
public class ProductCommandEvent {

    private Long orderId;
    private ProductCommandEventType eventType;
    private BigDecimal totalPrice;
    private List<ProductItemDTO> products = new ArrayList<>();
    private String message;
    private List<Map<String, Object>> errors = new ArrayList<>();
}
