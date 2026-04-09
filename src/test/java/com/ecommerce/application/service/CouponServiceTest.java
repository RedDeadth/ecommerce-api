package com.ecommerce.application.service;

import com.ecommerce.application.dto.CouponRequest;
import com.ecommerce.application.dto.CouponResponse;
import com.ecommerce.domain.model.Coupon;
import com.ecommerce.domain.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    private Coupon validCoupon;

    @BeforeEach
    void setUp() {
        validCoupon = Coupon.builder()
                .id(1L)
                .code("WELCOME10")
                .discountPercent(10)
                .maxUses(100)
                .currentUses(0)
                .minPurchase(BigDecimal.ZERO)
                .active(true)
                .expiresAt(LocalDateTime.now().plusDays(30))
                .build();
    }

    @Test
    void validate_shouldReturnCouponWhenValid() {
        when(couponRepository.findByCode("WELCOME10")).thenReturn(Optional.of(validCoupon));
        var result = couponService.validate("WELCOME10");
        assertEquals("WELCOME10", result.code());
        assertEquals(10, result.discountPercent());
    }

    @Test
    void validate_shouldThrowWhenCouponNotFound() {
        when(couponRepository.findByCode("FAKE")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> couponService.validate("FAKE"));
    }

    @Test
    void validate_shouldThrowWhenCouponExpired() {
        validCoupon.setExpiresAt(LocalDateTime.now().minusDays(1));
        when(couponRepository.findByCode("WELCOME10")).thenReturn(Optional.of(validCoupon));
        assertThrows(IllegalArgumentException.class, () -> couponService.validate("WELCOME10"));
    }

    @Test
    void validate_shouldThrowWhenMaxUsesReached() {
        validCoupon.setMaxUses(5);
        validCoupon.setCurrentUses(5);
        when(couponRepository.findByCode("WELCOME10")).thenReturn(Optional.of(validCoupon));
        assertThrows(IllegalArgumentException.class, () -> couponService.validate("WELCOME10"));
    }

    @Test
    void validate_shouldThrowWhenDeactivated() {
        validCoupon.setActive(false);
        when(couponRepository.findByCode("WELCOME10")).thenReturn(Optional.of(validCoupon));
        assertThrows(IllegalArgumentException.class, () -> couponService.validate("WELCOME10"));
    }

    @Test
    void useCoupon_shouldIncrementCounter() {
        when(couponRepository.findByCode("WELCOME10")).thenReturn(Optional.of(validCoupon));
        when(couponRepository.save(any(Coupon.class))).thenReturn(validCoupon);

        couponService.useCoupon("WELCOME10");
        assertEquals(1, validCoupon.getCurrentUses());
        verify(couponRepository).save(validCoupon);
    }

    @Test
    void useCoupon_shouldNotAllowDoubleUseAfterMaxReached() {
        validCoupon.setMaxUses(1);
        validCoupon.setCurrentUses(0);
        when(couponRepository.findByCode("WELCOME10")).thenReturn(Optional.of(validCoupon));
        when(couponRepository.save(any(Coupon.class))).thenReturn(validCoupon);

        // Primer uso: OK
        couponService.useCoupon("WELCOME10");
        assertEquals(1, validCoupon.getCurrentUses());

        // Segundo uso: debe fallar
        assertThrows(IllegalArgumentException.class, () -> couponService.useCoupon("WELCOME10"));
    }

    @Test
    void create_shouldThrowWhenCodeAlreadyExists() {
        when(couponRepository.existsByCode("WELCOME10")).thenReturn(true);
        var request = new CouponRequest("WELCOME10", 10, 100, BigDecimal.ZERO, null);
        assertThrows(IllegalArgumentException.class, () -> couponService.create(request));
    }
}
