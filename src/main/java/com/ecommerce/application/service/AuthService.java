package com.ecommerce.application.service;

import com.ecommerce.application.dto.*;
import com.ecommerce.domain.model.Role;
import com.ecommerce.domain.model.User;
import com.ecommerce.domain.repository.UserRepository;
import com.ecommerce.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final EmailService emailService;

    public TokenResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        var role = (request.role() != null && request.role().equalsIgnoreCase("ADMIN"))
                ? Role.ADMIN : Role.USER;

        var user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .role(role)
                .build();
        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

        emailService.sendWelcomeEmail(user.getEmail(), user.getFullName());

        return new TokenResponse(accessToken, refreshToken, user.getEmail(), user.getRole().name());
    }

    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

        return new TokenResponse(accessToken, refreshToken, user.getEmail(), user.getRole().name());
    }
}
