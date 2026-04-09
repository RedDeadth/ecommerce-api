package com.ecommerce.infrastructure.web.controller;

import com.ecommerce.application.dto.*;
import com.ecommerce.application.service.AuthService;
import com.ecommerce.application.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "1. Auth", description = "Registro, login y refresh tokens")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    @Operation(summary = "Registrar usuario", description = "Devuelve access + refresh token")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Devuelve access + refresh token")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refrescar token", description = "Intercambia un refresh token por nuevos access + refresh tokens")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(refreshTokenService.refreshAccessToken(request.refreshToken()));
    }
}
