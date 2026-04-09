package com.ecommerce.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Orden de compra")
public record OrderResponse(

        @Schema(description = "ID de la orden", example = "1")
        Long id,

        @Schema(description = "Total de la orden", example = "299.98")
        BigDecimal total,

        @Schema(description = "Estado de la orden", example = "CONFIRMED")
        String status,

        @Schema(description = "Fecha de creación")
        LocalDateTime createdAt,

        @Schema(description = "Items de la orden")
        List<OrderItemResponse> items

) {}
