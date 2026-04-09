package com.ecommerce.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Item dentro de una orden")
public record OrderItemResponse(

        @Schema(description = "ID del producto", example = "1")
        Long productId,

        @Schema(description = "Nombre del producto", example = "Wireless Headphones Pro")
        String productName,

        @Schema(description = "Cantidad comprada", example = "2")
        Integer quantity,

        @Schema(description = "Precio unitario al momento de la compra", example = "149.99")
        BigDecimal unitPrice

) {}
