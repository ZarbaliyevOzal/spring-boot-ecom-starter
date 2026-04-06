package org.example.product_service.product.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductFilterDTO {

    private String name;

//    @JsonProperty("min_price")
    private BigDecimal minPrice;

//    @JsonProperty("max_price")
    private BigDecimal maxPrice;

//    @JsonProperty("min_stock")
    private Integer minStock;

//    @JsonProperty("max_stock")
    private Integer maxStock;
}
