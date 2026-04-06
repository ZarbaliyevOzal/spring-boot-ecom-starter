package org.example.product_service.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductRequestDTO {

    @NotBlank
    private String name;

    private String description;

    @Positive(message = "Price must be bigger than 0")
    @NotNull
    private BigDecimal price;

    @JsonProperty("stock_quantity")
    @Min(value = 0)
    @NotNull
    private Integer stockQuantity;

    @JsonProperty("is_active")
    private Boolean isActive;
}
