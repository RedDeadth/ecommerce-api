package com.ecommerce.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Cupón de descuento")
public record CouponResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "WELCOME10") String code,
        @Schema(example = "10") Integer discountPercent,
        @Schema(example = "100") Integer maxUses,
        @Schema(example = "5") Integer currentUses,
        @Schema(example = "50.00") BigDecimal minPurchase,
        @Schema(example = "true") Boolean active,
        LocalDateTime expiresAt,
        LocalDateTime createdAt
) {}
