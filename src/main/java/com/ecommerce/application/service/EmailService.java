package com.ecommerce.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    /**
     * En producción: integrar con spring-boot-starter-mail + SMTP (Gmail, SendGrid, etc.)
     * Por ahora: log para demostrar la integración.
     */
    public void sendOrderConfirmation(String to, Long orderId, String total) {
        log.info("📧 EMAIL → {}: Orden #{} confirmada por ${}", to, orderId, total);
        // En producción:
        // MimeMessage msg = mailSender.createMimeMessage();
        // MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        // helper.setTo(to);
        // helper.setSubject("Orden #" + orderId + " confirmada");
        // helper.setText(buildHtmlTemplate(orderId, total), true);
        // mailSender.send(msg);
    }

    public void sendWelcomeEmail(String to, String name) {
        log.info("📧 EMAIL → {}: Bienvenido {}", to, name);
    }
}
