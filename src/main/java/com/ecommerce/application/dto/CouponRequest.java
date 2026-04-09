package com.ecommerce.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Crear cupón de descuento")
public record CouponRequest(
        @Schema(description = "Código del cupón", example = "WELCOME10") @NotBlank String code,
        @Schema(description = "Porcentaje de descuento (1-100)", example = "10") @NotNull @Min(1) @Max(100) Integer discountPercent,
        @Schema(description = "Usos máximos (0 = ilimitado)", example = "100") Integer maxUses,
        @Schema(description = "Compra mínima para aplicar", example = "50.00") BigDecimal minPurchase,
        @Schema(description = "Fecha de expiración") LocalDateTime expiresAt
) {}
