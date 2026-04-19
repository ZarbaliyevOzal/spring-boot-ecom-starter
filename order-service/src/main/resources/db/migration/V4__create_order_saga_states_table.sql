CREATE TABLE order_saga_states (
        id BIGSERIAL PRIMARY KEY,
        order_id BIGINT NOT NULL,
        event_type VARCHAR(100) NOT NULL,
        reason TEXT,
        created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

        CONSTRAINT fk_order_saga_states_order
            FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);