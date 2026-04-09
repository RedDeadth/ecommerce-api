package com.ecommerce.infrastructure.web.controller;

import com.ecommerce.application.dto.*;
import com.ecommerce.application.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "4c. Reviews", description = "Calificaciones y reseñas de productos")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/product/{productId}")
    @Operation(summary = "Ver reviews de un producto", description = "Acceso público")
    public ResponseEntity<List<ReviewResponse>> getProductReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getProductReviews(productId));
    }

    @GetMapping("/product/{productId}/rating")
    @Operation(summary = "Rating promedio de un producto", description = "Acceso público")
    public ResponseEntity<Map<String, Object>> getAverageRating(@PathVariable Long productId) {
        var avg = reviewService.getAverageRating(productId);
        return ResponseEntity.ok(Map.of("productId", productId, "averageRating", avg != null ? avg : 0));
    }

    @PostMapping
    @Operation(summary = "Crear review", description = "1 review por producto. Requiere autenticación")
    public ResponseEntity<ReviewResponse> createReview(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.createReview(user.getUsername(), request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar mi review")
    public ResponseEntity<Void> deleteReview(
            @AuthenticationPrincipal UserDetails user, @PathVariable Long id) {
        reviewService.deleteReview(user.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}
