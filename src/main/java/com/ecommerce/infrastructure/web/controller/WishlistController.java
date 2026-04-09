package com.ecommerce.infrastructure.web.controller;

import com.ecommerce.application.dto.WishlistResponse;
import com.ecommerce.application.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
@Tag(name = "4b. Wishlist", description = "Lista de deseos — requiere rol USER")
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping
    @Operation(summary = "Ver mi wishlist")
    public ResponseEntity<List<WishlistResponse>> getWishlist(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(wishlistService.getWishlist(user.getUsername()));
    }

    @PostMapping("/{productId}")
    @Operation(summary = "Agregar producto a wishlist")
    public ResponseEntity<WishlistResponse> add(@AuthenticationPrincipal UserDetails user, @PathVariable Long productId) {
        return ResponseEntity.ok(wishlistService.addToWishlist(user.getUsername(), productId));
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Quitar de wishlist")
    public ResponseEntity<Void> remove(@AuthenticationPrincipal UserDetails user, @PathVariable Long productId) {
        wishlistService.removeFromWishlist(user.getUsername(), productId);
        return ResponseEntity.noContent().build();
    }
}
