package com.ecommerce.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Item en el carrito de compras")
public record CartItemResponse(

        @Schema(description = "ID del item en carrito", example = "1")
        Long id,

        @Schema(description = "ID del producto", example = "1")
        Long productId,

        @Schema(description = "Nombre del producto", example = "Wireless Headphones Pro")
        String productName,

        @Schema(description = "Precio unitario", example = "149.99")
        BigDecimal productPrice,

        @Schema(description = "Cantidad", example = "2")
        Integer quantity,

        @Schema(description = "Subtotal (precio × cantidad)", example = "299.98")
        BigDecimal subtotal

) {}
