package org.example.order_service.order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderSagaStateRepository extends JpaRepository<OrderSagaState, Long>{
}
