package com.ecommerce.application.service;

import com.ecommerce.application.dto.OrderItemResponse;
import com.ecommerce.application.dto.OrderResponse;
import com.ecommerce.domain.model.*;
import com.ecommerce.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CouponService couponService;
    private final EmailService emailService;

    @Transactional
    public OrderResponse checkout(String email, String couponCode) {
        var user = userRepository.findByEmail(email).orElseThrow();
        var cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) throw new IllegalArgumentException("El carrito está vacío");

        var total = BigDecimal.ZERO;
        var order = Order.builder().user(user).total(BigDecimal.ZERO).status(OrderStatus.CONFIRMED).build();

        for (var cartItem : cartItems) {
            var product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para '%s'. Disponible: %d".formatted(product.getName(), product.getStock()));
            }
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            var orderItem = OrderItem.builder()
                    .product(product).quantity(cartItem.getQuantity()).unitPrice(product.getPrice()).build();
            order.addItem(orderItem);
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        // Aplicar cupón si se proporcionó
        if (couponCode != null && !couponCode.isBlank()) {
            var coupon = couponService.validate(couponCode);
            if (total.compareTo(new BigDecimal(coupon.minPurchase().toString())) >= 0) {
                var discount = total.multiply(BigDecimal.valueOf(coupon.discountPercent())).divide(BigDecimal.valueOf(100));
                total = total.subtract(discount);
                couponService.useCoupon(couponCode);
            }
        }

        order.setTotal(total);
        var savedOrder = orderRepository.save(order);
        cartItemRepository.deleteByUser(user);

        emailService.sendOrderConfirmation(user.getEmail(), savedOrder.getId(), total.toString());
        return toResponse(savedOrder);
    }

    @Transactional
    public OrderResponse updateStatus(Long orderId, String status) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));
        try {
            order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido: " + status);
        }
        return toResponse(orderRepository.save(order));
    }

    public List<OrderResponse> getUserOrders(String email) {
        var user = userRepository.findByEmail(email).orElseThrow();
        return orderRepository.findByUserOrderByCreatedAtDesc(user).stream().map(this::toResponse).toList();
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(this::toResponse).toList();
    }

    private OrderResponse toResponse(Order order) {
        var items = order.getItems().stream()
                .map(i -> new OrderItemResponse(i.getProduct().getId(), i.getProduct().getName(),
                        i.getQuantity(), i.getUnitPrice())).toList();
        return new OrderResponse(order.getId(), order.getTotal(), order.getStatus().name(),
                order.getCreatedAt(), items);
    }
}
