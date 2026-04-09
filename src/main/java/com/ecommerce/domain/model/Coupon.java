package com.ecommerce.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private Integer discountPercent;

    @Column(nullable = false)
    @Builder.Default
    private Integer maxUses = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer currentUses = 0;

    @Column(nullable = false)
    @Builder.Default
    private BigDecimal minPurchase = BigDecimal.ZERO;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    private LocalDateTime expiresAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    public boolean isValid() {
        if (!active) return false;
        if (maxUses > 0 && currentUses >= maxUses) return false;
        if (expiresAt != null && LocalDateTime.now().isAfter(expiresAt)) return false;
        return true;
    }
}
