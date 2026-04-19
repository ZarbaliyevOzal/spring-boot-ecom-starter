package org.example.order_service.order.messaging.commands;

import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class NotificationCommand {

    private Long orderId;
    private Long userId;
    private Instant createdAt;
}
