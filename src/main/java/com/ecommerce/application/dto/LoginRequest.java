package com.ecommerce.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciales de inicio de sesión")
public record LoginRequest(

        @Schema(description = "Email registrado", example = "admin@ecommerce.com")
        @NotBlank @Email
        String email,

        @Schema(description = "Contraseña", example = "admin123")
        @NotBlank
        String password

) {}
