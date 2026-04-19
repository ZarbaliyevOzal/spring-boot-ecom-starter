package org.example.order_service.order.type;

public enum OrderEventType {
    ORDER_CREATED,
    PRODUCT_RESERVED,
    PAYMENT_PROCESSED,
    PAYMENT_FAILED,
    PRODUCT_RELEASED,
    ORDER_CANCELLED,
    ORDER_COMPLETED,
    PRODUCT_FAILED
}
