package com.ecommerce.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "Datos para crear o editar un producto")
public record ProductRequest(

        @Schema(description = "Nombre del producto", example = "Gaming Mouse Pro")
        @NotBlank
        String name,

        @Schema(description = "Descripción detallada", example = "Mouse ergonómico con sensor de 12000 DPI y 7 botones programables")
        String description,

        @Schema(description = "Precio en USD", example = "59.99")
        @NotNull @DecimalMin("0.01")
        BigDecimal price,

        @Schema(description = "Cantidad en inventario", example = "100")
        @NotNull @Min(0)
        Integer stock,

        @Schema(description = "URL de la imagen", example = "https://placehold.co/400x400/1a1a2e/D4AF37?text=Mouse")
        String imageUrl,

        @Schema(description = "Categoría del producto", example = "Electronics")
        String category

) {}
