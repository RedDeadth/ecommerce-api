package com.ecommerce.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de autenticación con token JWT")
public record AuthResponse(

        @Schema(description = "Token JWT para autenticación", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,

        @Schema(description = "Email del usuario autenticado", example = "admin@ecommerce.com")
        String email,

        @Schema(description = "Rol asignado", example = "ADMIN")
        String role

) {}
