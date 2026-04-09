package com.ecommerce.infrastructure.web.controller;

import com.ecommerce.application.dto.*;
import com.ecommerce.application.service.CouponService;
import com.ecommerce.application.service.OrderService;
import com.ecommerce.application.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;
    private final OrderService orderService;
    private final CouponService couponService;

    // ─── 3. Admin — Productos ──────────────────────

    @GetMapping("/products")
    @Tag(name = "3. Admin — Productos")
    @Operation(summary = "Listar todos los productos", description = "Incluye inactivos")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAll());
    }

    @PostMapping("/products")
    @Tag(name = "3. Admin — Productos")
    @Operation(summary = "Crear producto")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.create(request));
    }

    @PutMapping("/products/{id}")
    @Tag(name = "3. Admin — Productos")
    @Operation(summary = "Editar producto")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @DeleteMapping("/products/{id}")
    @Tag(name = "3. Admin — Productos")
    @Operation(summary = "Eliminar producto (soft delete)")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ─── 6. Admin — Órdenes ──────────────────────

    @GetMapping("/orders")
    @Tag(name = "6. Admin — Órdenes")
    @Operation(summary = "Ver todas las órdenes")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PatchMapping("/orders/{id}/status")
    @Tag(name = "6. Admin — Órdenes")
    @Operation(summary = "Cambiar estado de orden", description = "PENDING → CONFIRMED → SHIPPED → DELIVERED → CANCELLED")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id, @Valid @RequestBody OrderStatusRequest request) {
        return ResponseEntity.ok(orderService.updateStatus(id, request.status()));
    }

    // ─── 7. Admin — Cupones ──────────────────────

    @GetMapping("/coupons")
    @Tag(name = "7. Admin — Cupones")
    @Operation(summary = "Listar cupones")
    public ResponseEntity<List<CouponResponse>> getAllCoupons() {
        return ResponseEntity.ok(couponService.getAll());
    }

    @PostMapping("/coupons")
    @Tag(name = "7. Admin — Cupones")
    @Operation(summary = "Crear cupón de descuento")
    public ResponseEntity<CouponResponse> createCoupon(@Valid @RequestBody CouponRequest request) {
        return ResponseEntity.ok(couponService.create(request));
    }

    @DeleteMapping("/coupons/{id}")
    @Tag(name = "7. Admin — Cupones")
    @Operation(summary = "Desactivar cupón")
    public ResponseEntity<Void> deactivateCoupon(@PathVariable Long id) {
        couponService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/coupons/validate/{code}")
    @Tag(name = "7. Admin — Cupones")
    @Operation(summary = "Validar cupón")
    public ResponseEntity<CouponResponse> validateCoupon(@PathVariable String code) {
        return ResponseEntity.ok(couponService.validate(code));
    }
}
