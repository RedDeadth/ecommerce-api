package com.ecommerce.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Crear una review de producto")
public record ReviewRequest(
        @Schema(description = "ID del producto", example = "1") @NotNull Long productId,
        @Schema(description = "Rating de 1 a 5", example = "5") @NotNull @Min(1) @Max(5) Integer rating,
        @Schema(description = "Comentario", example = "Excelente producto, muy buena calidad") String comment
) {}
