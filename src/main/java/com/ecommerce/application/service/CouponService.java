package com.ecommerce.application.service;

import com.ecommerce.application.dto.CouponRequest;
import com.ecommerce.application.dto.CouponResponse;
import com.ecommerce.domain.model.Coupon;
import com.ecommerce.domain.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public List<CouponResponse> getAll() {
        return couponRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public CouponResponse create(CouponRequest request) {
        if (couponRepository.existsByCode(request.code().toUpperCase())) {
            throw new IllegalArgumentException("El código de cupón ya existe");
        }
        var coupon = Coupon.builder()
                .code(request.code().toUpperCase())
                .discountPercent(request.discountPercent())
                .maxUses(request.maxUses() != null ? request.maxUses() : 0)
                .minPurchase(request.minPurchase() != null ? request.minPurchase() : BigDecimal.ZERO)
                .expiresAt(request.expiresAt())
                .build();
        return toResponse(couponRepository.save(coupon));
    }

    public CouponResponse validate(String code) {
        var coupon = couponRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Cupón no encontrado"));
        if (!coupon.isValid()) {
            throw new IllegalArgumentException("El cupón no es válido o ha expirado");
        }
        return toResponse(coupon);
    }

    @Transactional
    public void useCoupon(String code) {
        var coupon = couponRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Cupón no encontrado"));
        if (!coupon.isValid()) throw new IllegalArgumentException("Cupón inválido");
        coupon.setCurrentUses(coupon.getCurrentUses() + 1);
        couponRepository.save(coupon);
    }

    @Transactional
    public void deactivate(Long id) {
        var coupon = couponRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cupón no encontrado"));
        coupon.setActive(false);
        couponRepository.save(coupon);
    }

    private CouponResponse toResponse(Coupon c) {
        return new CouponResponse(c.getId(), c.getCode(), c.getDiscountPercent(),
                c.getMaxUses(), c.getCurrentUses(), c.getMinPurchase(),
                c.getActive(), c.getExpiresAt(), c.getCreatedAt());
    }
}
