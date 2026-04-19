package org.example.order_service.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.order_service.config.UserPrincipal;
import org.example.order_service.order.dto.OrderRequestDTO;
import org.example.order_service.order.dto.OrderResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                        @RequestBody @Valid OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO orderResponseDTO = orderService.createOrder(userPrincipal.getUserId(), orderRequestDTO);
        return ResponseEntity.ok().body(orderResponseDTO);
    }
}
