-- V2__seed_data.sql — Admin user + sample products

-- Admin user (password: admin123 — BCrypt hash)
INSERT INTO users (email, password, full_name, role)
VALUES ('admin@ecommerce.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin', 'ADMIN');

-- Sample products
INSERT INTO products (name, description, price, stock, image_url, category) VALUES
('Wireless Headphones Pro', 'Premium noise-cancelling headphones with 40h battery life', 149.99, 50, 'https://placehold.co/400x400/222/gold?text=Headphones', 'Electronics'),
('Leather Messenger Bag', 'Handcrafted genuine leather bag with laptop compartment', 89.99, 30, 'https://placehold.co/400x400/222/gold?text=Bag', 'Accessories'),
('Smart Watch Elite', 'AMOLED display, heart rate monitor, 7-day battery', 299.99, 25, 'https://placehold.co/400x400/222/gold?text=Watch', 'Electronics'),
('Minimalist Desk Lamp', 'USB-C powered LED lamp with 3 color temperatures', 45.99, 100, 'https://placehold.co/400x400/222/gold?text=Lamp', 'Home'),
('Mechanical Keyboard RGB', 'Cherry MX switches, hot-swappable, aluminum frame', 129.99, 40, 'https://placehold.co/400x400/222/gold?text=Keyboard', 'Electronics');
