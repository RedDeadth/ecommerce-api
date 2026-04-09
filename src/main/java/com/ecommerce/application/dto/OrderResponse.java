package com.ecommerce.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        BigDecimal total,
        String status,
        LocalDateTime createdAt,
        List<OrderItemResponse> items
) {}
