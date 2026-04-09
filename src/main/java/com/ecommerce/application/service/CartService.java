package com.ecommerce.application.service;

import com.ecommerce.application.dto.CartItemRequest;
import com.ecommerce.application.dto.CartItemResponse;
import com.ecommerce.domain.model.CartItem;
import com.ecommerce.domain.model.Product;
import com.ecommerce.domain.model.User;
import com.ecommerce.domain.repository.CartItemRepository;
import com.ecommerce.domain.repository.ProductRepository;
import com.ecommerce.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<CartItemResponse> getCart(String email) {
        var user = findUser(email);
        return cartItemRepository.findByUser(user).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CartItemResponse addToCart(String email, CartItemRequest request) {
        var user = findUser(email);
        var product = productRepository.findById(request.productId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        if (!product.getActive()) {
            throw new IllegalArgumentException("Producto no disponible");
        }

        if (product.getStock() < request.quantity()) {
            throw new IllegalArgumentException("Stock insuficiente. Disponible: " + product.getStock());
        }

        var existing = cartItemRepository.findByUserAndProductId(user, product.getId());
        if (existing.isPresent()) {
            var item = existing.get();
            int newQty = item.getQuantity() + request.quantity();
            if (product.getStock() < newQty) {
                throw new IllegalArgumentException("Stock insuficiente para la cantidad total");
            }
            item.setQuantity(newQty);
            return toResponse(cartItemRepository.save(item));
        }

        var cartItem = CartItem.builder()
                .user(user)
                .product(product)
                .quantity(request.quantity())
                .build();
        return toResponse(cartItemRepository.save(cartItem));
    }

    @Transactional
    public void removeFromCart(String email, Long cartItemId) {
        var user = findUser(email);
        var item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Item no encontrado"));

        if (!item.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("No autorizado para eliminar este item");
        }
        cartItemRepository.delete(item);
    }

    @Transactional
    public void clearCart(String email) {
        var user = findUser(email);
        cartItemRepository.deleteByUser(user);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private CartItemResponse toResponse(CartItem item) {
        var p = item.getProduct();
        var subtotal = p.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        return new CartItemResponse(
                item.getId(), p.getId(), p.getName(),
                p.getPrice(), item.getQuantity(), subtotal
        );
    }
}
