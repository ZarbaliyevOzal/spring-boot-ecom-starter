CREATE TABLE order_items (
     id BIGSERIAL PRIMARY KEY,
     order_id BIGINT NOT NULL,
     product_id BIGINT NOT NULL,
     quantity INTEGER NOT NULL CHECK (quantity > 0),
     unit_price NUMERIC(12, 2) NOT NULL,
     total_price NUMERIC(12, 2) GENERATED ALWAYS AS (quantity * unit_price) STORED,
     created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
     deleted_at TIMESTAMPTZ,

     CONSTRAINT fk_order_items_order
         FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);