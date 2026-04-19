package org.example.order_service.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Generated;
import org.hibernate.boot.jaxb.mapping.GenerationTiming;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "order_items")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    private Long productId;

    private Integer quantity;

    private BigDecimal unitPrice;

    @Generated
    @Column(insertable = false, updatable = false,
            columnDefinition = "GENERATED ALWAYS AS (quantity * unit_price) STORED")
    private BigDecimal totalPrice;

    @Column(nullable = false, insertable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false, insertable = false)
    private Instant updatedAt;

    private Instant deletedAt;
}
