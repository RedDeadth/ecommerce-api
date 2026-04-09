package com.ecommerce.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Review de un producto")
public record ReviewResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "1") Long productId,
        @Schema(example = "Juan Pérez") String userName,
        @Schema(example = "5") Integer rating,
        @Schema(example = "Excelente producto") String comment,
        LocalDateTime createdAt
) {}
