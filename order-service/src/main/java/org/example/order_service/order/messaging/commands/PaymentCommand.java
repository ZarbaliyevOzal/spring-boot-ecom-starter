package org.example.order_service.order.messaging.commands;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PaymentCommand {

    private Long orderId;
    private BigDecimal orderTotalPrice;
    private Instant createdAt;
}
