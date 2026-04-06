package org.example.product_service.product;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(precision = 10, scale = 2, nullable = false, columnDefinition = "DEFAULT 0 CHECK (price >= 0)")
    private BigDecimal price;

    @Column(nullable = false, columnDefinition = "DEFAULT 0 CHECK (stock_quantity >= 0)")
    private Integer stockQuantity;

    @Column(columnDefinition = "DEFAULT TRUE")
    private Boolean isActive;

    @Column(nullable = false, insertable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false, insertable = false)
    private Instant updatedAt;

    private Instant deletedAt;
}
