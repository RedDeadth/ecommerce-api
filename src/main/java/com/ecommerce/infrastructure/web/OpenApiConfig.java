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
                                1. **Registrar** usuario o admin → `POST /api/auth/register`
                                2. **Login** → `POST /api/auth/login` → copiar el token JWT
                                3. **Authorize** → Clic en el botón 🔒 Authorize y pegar el token
                                4. **Explorar** endpoints según tu rol
                                
                                ## Credenciales de prueba
                                | Email | Password | Rol |
                                |---|---|---|
                                | `admin@ecommerce.com` | `admin123` | ADMIN |
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Ecommerce Team")
                                .email("admin@ecommerce.com")))
                // Orden de tags = flujo de historia de usuario
                .tags(List.of(
                        new Tag().name("1. Auth").description("Registro y autenticación — obtener JWT"),
                        new Tag().name("2. Productos (Público)").description("Catálogo — acceso sin autenticación"),
                        new Tag().name("3. Admin — Productos").description("CRUD de productos — requiere rol ADMIN"),
                        new Tag().name("4. Carrito").description("Gestión del carrito — requiere rol USER"),
                        new Tag().name("5. Órdenes").description("Checkout y historial — requiere rol USER"),
                        new Tag().name("6. Admin — Órdenes").description("Vista de todas las órdenes — requiere rol ADMIN")
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Pega aquí el token del login")));
    }
}
