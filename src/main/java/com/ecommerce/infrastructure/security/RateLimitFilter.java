package com.ecommerce.infrastructure.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Rate limiter in-memory simple.
 * Limita a 60 requests por minuto por IP.
 * En producción: usar Redis + Bucket4j.
 */
@Component
@Order(1)
public class RateLimitFilter implements Filter {

    private static final int MAX_REQUESTS = 60;
    private static final long WINDOW_MS = 60_000;

    private final Map<String, RateInfo> rateLimits = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        var httpReq = (HttpServletRequest) request;
        var ip = getClientIP(httpReq);

        var rateInfo = rateLimits.compute(ip, (key, info) -> {
            if (info == null || System.currentTimeMillis() - info.windowStart > WINDOW_MS) {
                return new RateInfo(System.currentTimeMillis(), new AtomicLong(1));
            }
            info.count.incrementAndGet();
            return info;
        });

        if (rateInfo.count.get() > MAX_REQUESTS) {
            var httpRes = (HttpServletResponse) response;
            httpRes.setStatus(429);
            httpRes.setContentType("application/json");
            httpRes.getWriter().write("{\"error\":\"Rate limit exceeded. Max %d requests per minute.\"}".formatted(MAX_REQUESTS));
            return;
        }

        chain.doFilter(request, response);
    }

    private String getClientIP(HttpServletRequest request) {
        var xff = request.getHeader("X-Forwarded-For");
        return (xff != null && !xff.isBlank()) ? xff.split(",")[0].trim() : request.getRemoteAddr();
    }

    private record RateInfo(long windowStart, AtomicLong count) {}
}
