package com.ecommerce.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Cambiar estado de una orden")
public record OrderStatusRequest(
        @Schema(description = "Nuevo estado: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED", example = "SHIPPED")
        @NotBlank String status
) {}
