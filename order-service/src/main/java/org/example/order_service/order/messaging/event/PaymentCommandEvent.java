package org.example.order_service.order.messaging.event;

import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PaymentCommandEvent {

    private Long orderId;
    private Boolean success;
    private Instant createdAt;
    private String message;
}
