package com.ecommerce.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta con access + refresh tokens")
public record TokenResponse(
        @Schema(description = "Access token JWT") String accessToken,
        @Schema(description = "Refresh token") String refreshToken,
        @Schema(example = "admin@ecommerce.com") String email,
        @Schema(example = "ADMIN") String role
) {}
