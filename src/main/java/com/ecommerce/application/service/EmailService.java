package com.ecommerce.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    public void sendOrderConfirmation(String to, Long orderId, String total) {
        log.info("📧 EMAIL → {}: Orden #{} confirmada por ${}", to, orderId, total);
    }

    public void sendWelcomeEmail(String to, String name) {
        log.info("📧 EMAIL → {}: Bienvenido {}", to, name);
    }
}
