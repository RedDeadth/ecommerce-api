package com.ecommerce.infrastructure.web.controller;

import com.ecommerce.application.dto.*;
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

    // ─── 3. Admin — Productos ──────────────────────

    @GetMapping("/products")
    @Tag(name = "3. Admin — Productos")
    @Operation(summary = "Listar todos los productos", description = "Incluye productos inactivos")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAll());
    }

    @PostMapping("/products")
    @Tag(name = "3. Admin — Productos")
    @Operation(summary = "Crear producto", description = "Solo ADMIN puede crear productos")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.create(request));
    }

    @PutMapping("/products/{id}")
    @Tag(name = "3. Admin — Productos")
    @Operation(summary = "Editar producto")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @DeleteMapping("/products/{id}")
    @Tag(name = "3. Admin — Productos")
    @Operation(summary = "Eliminar producto", description = "Soft delete — marca como inactivo")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ─── 6. Admin — Órdenes ──────────────────────

    @GetMapping("/orders")
    @Tag(name = "6. Admin — Órdenes")
    @Operation(summary = "Ver todas las órdenes", description = "Vista de administrador de todas las órdenes del sistema")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}
