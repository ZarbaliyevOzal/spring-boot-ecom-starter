package org.example.product_service.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UpdateProductRequestDTO {

    private String name;
    private String description;

    @Positive(message = "Price must be bigger than 0")
    private BigDecimal price;

    @JsonProperty("stock_quantity")
    @Min(value = 0)
    private Integer stockQuantity;

    @JsonProperty("is_active")
    private Boolean isActive;
}
