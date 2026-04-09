package com.ecommerce.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Producto en la wishlist")
public record WishlistResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "1") Long productId,
        @Schema(example = "Wireless Headphones Pro") String productName,
        @Schema(example = "149.99") java.math.BigDecimal productPrice,
        @Schema(example = "https://placehold.co/400") String productImageUrl
) {}
