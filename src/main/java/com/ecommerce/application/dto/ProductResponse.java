package com.ecommerce.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Información de un producto")
public record ProductResponse(

        @Schema(description = "ID del producto", example = "1")
        Long id,

        @Schema(description = "Nombre", example = "Wireless Headphones Pro")
        String name,

        @Schema(description = "Descripción", example = "Premium noise-cancelling headphones")
        String description,

        @Schema(description = "Precio", example = "149.99")
        BigDecimal price,

        @Schema(description = "Stock disponible", example = "50")
        Integer stock,

        @Schema(description = "URL de imagen", example = "https://placehold.co/400x400")
        String imageUrl,

        @Schema(description = "Categoría", example = "Electronics")
        String category,

        @Schema(description = "Producto activo", example = "true")
        Boolean active,

        @Schema(description = "Fecha de creación")
        LocalDateTime createdAt

) {}
