package com.ecommerce.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RateLimitFilterTest {

    private RateLimitFilter rateLimitFilter;

    @BeforeEach
    void setUp() {
        rateLimitFilter = new RateLimitFilter();
    }

    @Test
    void shouldAllowRequestsWithinLimit() throws Exception {
        var request = mockRequest("192.168.1.1");
        var response = mock(HttpServletResponse.class);
        var chain = mock(FilterChain.class);

        // 60 requests deberían pasar
        for (int i = 0; i < 60; i++) {
            rateLimitFilter.doFilter(request, response, chain);
        }

        verify(chain, times(60)).doFilter(request, response);
    }

    @Test
    void shouldBlockRequestsExceedingLimit() throws Exception {
        var request = mockRequest("10.0.0.1");
        var chain = mock(FilterChain.class);

        // Enviar 60 requests válidas
        for (int i = 0; i < 60; i++) {
            var okResponse = mock(HttpServletResponse.class);
            rateLimitFilter.doFilter(request, okResponse, chain);
        }

        // La request #61 debería ser bloqueada con 429
        var blockedResponse = mock(HttpServletResponse.class);
        var writer = new StringWriter();
        when(blockedResponse.getWriter()).thenReturn(new PrintWriter(writer));

        rateLimitFilter.doFilter(request, blockedResponse, chain);

        verify(blockedResponse).setStatus(429);
        assertTrue(writer.toString().contains("Rate limit exceeded"));
        // chain.doFilter NO debería ser llamado para la request #61
        verify(chain, times(60)).doFilter(eq(request), any());
    }

    @Test
    void shouldTrackDifferentIPsSeparately() throws Exception {
        var request1 = mockRequest("1.1.1.1");
        var request2 = mockRequest("2.2.2.2");
        var response = mock(HttpServletResponse.class);
        var chain = mock(FilterChain.class);

        // 60 requests de IP1
        for (int i = 0; i < 60; i++) {
            rateLimitFilter.doFilter(request1, response, chain);
        }

        // IP2 aún debería poder hacer requests
        rateLimitFilter.doFilter(request2, response, chain);
        verify(chain, times(61)).doFilter(any(), eq(response));
    }

    @Test
    void couponSpamShouldBeBlocked() throws Exception {
        // Simula que un usuario spammea el endpoint de cupones
        var request = mockRequest("10.10.10.10");
        var chain = mock(FilterChain.class);

        // Enviar 61 requests rápidas (simula spam de validar/usar cupón)
        for (int i = 0; i < 61; i++) {
            var resp = mock(HttpServletResponse.class);
            if (i == 60) {
                // La última debería tener writer para capturar error
                when(resp.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
            }
            rateLimitFilter.doFilter(request, resp, chain);

            if (i == 60) {
                verify(resp).setStatus(429);
            }
        }

        // Solo 60 deberían haber pasado al chain
        verify(chain, times(60)).doFilter(eq(request), any());
    }

    private HttpServletRequest mockRequest(String ip) {
        var request = mock(HttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn(ip);
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        return request;
    }
}
