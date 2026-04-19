package org.example.order_service.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.order_service.order.type.OrderEventType;

import java.time.Instant;

@Entity
@Table(name = "order_saga_states")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class OrderSagaState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    @Enumerated(EnumType.STRING)
    private OrderEventType eventType;

    private String reason;

    @Column(nullable = false, insertable = false, updatable = false)
    private Instant createdAt;
}
