package org.example.order_service.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.example.order_service.order.type.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderSagaState> orderSagaStates = new ArrayList<>();

    @Column(nullable = false, insertable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false, insertable = false)
    private Instant updatedAt;

    private Instant deletedAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.CREATED;

    public void addOrderItem(OrderItem item) {
        item.setOrder(this); // Important: set back reference
        orderItems.add(item);
    }
}
