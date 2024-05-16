
CREATE TABLE IF NOT EXISTS payment
(
    payment_order_number SERIAL PRIMARY KEY,
    payment_external_order_id TEXT NOT NULL,
    payment_external_order_global_id TEXT,
    payment_payment_info TEXT NOT NULL,
    payment_created_at TIMESTAMP NOT NULL,
    payment_status TEXT NOT NULL,
    payment_status_changed_at TIMESTAMP NOT NULL
);
