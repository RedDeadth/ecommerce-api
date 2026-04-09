-- V3__advanced_features.sql — Wishlist, Reviews, Coupons, Refresh Tokens

CREATE TABLE wishlist_items (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (product_id) REFERENCES products (id),
    UNIQUE (user_id, product_id)
);

CREATE TABLE reviews (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT      NOT NULL,
    product_id BIGINT      NOT NULL,
    rating     INT         NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment    VARCHAR(1000),
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (product_id) REFERENCES products (id),
    UNIQUE (user_id, product_id)
);

CREATE TABLE coupons (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    code             VARCHAR(50)    NOT NULL UNIQUE,
    discount_percent INT            NOT NULL CHECK (discount_percent BETWEEN 1 AND 100),
    max_uses         INT            NOT NULL DEFAULT 0,
    current_uses     INT            NOT NULL DEFAULT 0,
    min_purchase     DECIMAL(10, 2) NOT NULL DEFAULT 0,
    active           BOOLEAN        NOT NULL DEFAULT TRUE,
    expires_at       TIMESTAMP,
    created_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE refresh_tokens (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT       NOT NULL,
    token      VARCHAR(500) NOT NULL UNIQUE,
    expires_at TIMESTAMP    NOT NULL,
    revoked    BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
