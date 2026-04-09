package com.ecommerce.infrastructure.web.controller;

import com.ecommerce.application.dto.OrderResponse;
import com.ecommerce.application.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "5. Órdenes", description = "Checkout y historial de órdenes — requiere rol USER")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    @Operation(summary = "Realizar compra", description = "Convierte el carrito actual en una orden y descuenta inventario")
    public ResponseEntity<OrderResponse> checkout(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(orderService.checkout(user.getUsername()));
    }

    @GetMapping
    @Operation(summary = "Mi historial de órdenes")
    public ResponseEntity<List<OrderResponse>> getMyOrders(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(orderService.getUserOrders(user.getUsername()));
    }
}
