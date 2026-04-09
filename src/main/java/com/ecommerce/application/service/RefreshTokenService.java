package com.ecommerce.application.service;

import com.ecommerce.application.dto.TokenResponse;
import com.ecommerce.domain.model.RefreshToken;
import com.ecommerce.domain.repository.RefreshTokenRepository;
import com.ecommerce.domain.repository.UserRepository;
import com.ecommerce.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Transactional
    public String createRefreshToken(String email) {
        var user = userRepository.findByEmail(email).orElseThrow();
        var token = UUID.randomUUID().toString();
        var refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    @Transactional
    public TokenResponse refreshAccessToken(String refreshTokenStr) {
        var refreshToken = refreshTokenRepository.findByToken(refreshTokenStr)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token inválido"));

        if (refreshToken.getRevoked() || refreshToken.isExpired()) {
            throw new IllegalArgumentException("Refresh token expirado o revocado");
        }

        var user = refreshToken.getUser();
        var userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        var newAccessToken = jwtService.generateToken(userDetails);

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
        var newRefreshToken = createRefreshToken(user.getEmail());

        return new TokenResponse(newAccessToken, newRefreshToken, user.getEmail(), user.getRole().name());
    }

    @Transactional
    public void revokeAllTokens(String email) {
        var user = userRepository.findByEmail(email).orElseThrow();
        refreshTokenRepository.deleteByUserId(user.getId());
    }
}
