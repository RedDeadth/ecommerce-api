package com.ecommerce.application.service;

import com.ecommerce.application.dto.WishlistResponse;
import com.ecommerce.domain.model.WishlistItem;
import com.ecommerce.domain.repository.ProductRepository;
import com.ecommerce.domain.repository.UserRepository;
import com.ecommerce.domain.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public List<WishlistResponse> getWishlist(String email) {
        var user = userRepository.findByEmail(email).orElseThrow();
        return wishlistRepository.findByUser(user).stream()
                .map(w -> new WishlistResponse(w.getId(), w.getProduct().getId(),
                        w.getProduct().getName(), w.getProduct().getPrice(), w.getProduct().getImageUrl()))
                .toList();
    }

    @Transactional
    public WishlistResponse addToWishlist(String email, Long productId) {
        var user = userRepository.findByEmail(email).orElseThrow();
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        if (wishlistRepository.existsByUserAndProductId(user, productId)) {
            throw new IllegalArgumentException("El producto ya está en tu wishlist");
        }

        var item = WishlistItem.builder().user(user).product(product).build();
        var saved = wishlistRepository.save(item);
        return new WishlistResponse(saved.getId(), product.getId(),
                product.getName(), product.getPrice(), product.getImageUrl());
    }

    @Transactional
    public void removeFromWishlist(String email, Long productId) {
        var user = userRepository.findByEmail(email).orElseThrow();
        var item = wishlistRepository.findByUserAndProductId(user, productId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no está en la wishlist"));
        wishlistRepository.delete(item);
    }
}
