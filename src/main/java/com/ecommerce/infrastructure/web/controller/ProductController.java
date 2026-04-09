package com.ecommerce.infrastructure.web.controller;

import com.ecommerce.application.dto.ProductResponse;
import com.ecommerce.application.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Productos (Público)", description = "Catálogo de productos — acceso público")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Listar productos activos", description = "Opcionalmente filtrar por categoría con ?category=Electronics")
    public ResponseEntity<List<ProductResponse>> getAll(
            @RequestParam(required = false) String category) {
        if (category != null && !category.isBlank()) {
            return ResponseEntity.ok(productService.getByCategory(category));
        }
        return ResponseEntity.ok(productService.getAllActive());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalle de producto")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }
}
