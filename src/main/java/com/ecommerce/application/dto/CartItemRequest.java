package com.ecommerce.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Agregar producto al carrito")
public record CartItemRequest(

        @Schema(description = "ID del producto a agregar", example = "1")
        @NotNull
        Long productId,

        @Schema(description = "Cantidad", example = "2")
        @NotNull @Min(1)
        Integer quantity

) {}
