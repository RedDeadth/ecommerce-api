package com.ecommerce.infrastructure.web.controller;

import com.ecommerce.application.dto.CartItemRequest;
import com.ecommerce.application.dto.CartItemResponse;
import com.ecommerce.application.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "4. Carrito", description = "Carrito de compras — requiere rol USER")
public class CartController {

    private final CartService cartService;

    @GetMapping
    @Operation(summary = "Ver mi carrito")
    public ResponseEntity<List<CartItemResponse>> getCart(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(cartService.getCart(user.getUsername()));
    }

    @PostMapping
    @Operation(summary = "Agregar producto al carrito")
    public ResponseEntity<CartItemResponse> addToCart(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addToCart(user.getUsername(), request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Quitar item del carrito")
    public ResponseEntity<Void> removeFromCart(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long id) {
        cartService.removeFromCart(user.getUsername(), id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(summary = "Vaciar carrito")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UserDetails user) {
        cartService.clearCart(user.getUsername());
        return ResponseEntity.noContent().build();
    }
}
