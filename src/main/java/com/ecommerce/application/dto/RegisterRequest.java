package com.ecommerce.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request para registro de nuevo usuario")
public record RegisterRequest(

        @Schema(description = "Email del usuario", example = "usuario@gmail.com")
        @NotBlank @Email
        String email,

        @Schema(description = "Contraseña (mínimo 6 caracteres)", example = "miPassword123")
        @NotBlank @Size(min = 6)
        String password,

        @Schema(description = "Nombre completo", example = "Juan Pérez")
        @NotBlank
        String fullName,

        @Schema(description = "Rol del usuario: USER (default) o ADMIN", example = "USER")
        String role

) {}
