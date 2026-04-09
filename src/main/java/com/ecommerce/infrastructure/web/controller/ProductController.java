package com.ecommerce.infrastructure.web.controller;

import com.ecommerce.application.dto.ProductResponse;
import com.ecommerce.application.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "2. Productos (Público)", description = "Catálogo con paginación y búsqueda")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Listar productos (paginado)", description = "Filtros por categoría, búsqueda por nombre, rango de precios")
    public ResponseEntity<Page<ProductResponse>> getAll(
            @Parameter(description = "Buscar por nombre") @RequestParam(required = false) String name,
            @Parameter(description = "Filtrar por categoría") @RequestParam(required = false) String category,
            @Parameter(description = "Precio mínimo") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Precio máximo") @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Página (desde 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Ordenar por: name, price, createdAt") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Dirección: asc o desc") @RequestParam(defaultValue = "desc") String direction) {

        var sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        var pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(productService.search(name, category, minPrice, maxPrice, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalle de producto")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }
}
