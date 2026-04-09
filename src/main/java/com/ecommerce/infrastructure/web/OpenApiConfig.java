package com.ecommerce.infrastructure.web;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "Bearer JWT";

        return new OpenAPI()
                .info(new Info()
                        .title("🛒 Ecommerce API")
                        .description("""
                                API REST de Ecommerce con roles **Admin** y **Usuario**.
                                
                                ## Flujo de uso
                                1. **Login** → `POST /api/auth/login` → copia el `accessToken`
                                2. **Authorize** → Clic en 🔒 → pegar el token
                                3. **Explorar** endpoints según tu rol
                                
                                ## Credenciales
                                | Email | Password | Rol |
                                |---|---|---|
                                | `admin@ecommerce.com` | `admin123` | ADMIN |
                                
                                ## Features
                                ✅ JWT + Refresh Tokens · ✅ Paginación · ✅ Búsqueda avanzada  
                                ✅ Wishlist · ✅ Reviews/Ratings · ✅ Cupones de descuento  
                                ✅ Gestión de órdenes · ✅ Rate Limiting (60 req/min)
                                """)
                        .version("2.0.0")
                        .contact(new Contact().name("Ecommerce Team").email("admin@ecommerce.com")))
                .tags(List.of(
                        new Tag().name("1. Auth").description("Registro, login y refresh tokens"),
                        new Tag().name("2. Productos (Público)").description("Catálogo con paginación y búsqueda"),
                        new Tag().name("3. Admin — Productos").description("CRUD de productos — rol ADMIN"),
                        new Tag().name("4. Carrito").description("Carrito de compras — rol USER"),
                        new Tag().name("4b. Wishlist").description("Lista de deseos — rol USER"),
                        new Tag().name("4c. Reviews").description("Calificaciones y reseñas"),
                        new Tag().name("5. Órdenes").description("Checkout con cupones — rol USER"),
                        new Tag().name("6. Admin — Órdenes").description("Gestión de órdenes — rol ADMIN"),
                        new Tag().name("7. Admin — Cupones").description("Gestión de cupones de descuento — rol ADMIN")
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName).type(SecurityScheme.Type.HTTP)
                                .scheme("bearer").bearerFormat("JWT")
                                .description("Pega aquí el accessToken del login")));
    }
}
