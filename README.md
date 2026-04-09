# 🛒 E-Commerce API REST (Producción)

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Hexagonal Architecture](https://img.shields.io/badge/Architecture-Hexagonal-8A2BE2?style=for-the-badge)

API REST Backend para un e-commerce construido para cumplir con todos los estándares modernos de ingeniería de software para despliegue en entornos de producción reales.

## ✨ Características Avanzadas (Level "Senior")

A diferencia de proyectos tutoriales básicos, este backend implementa:

- 🏗️ **Arquitectura Hexagonal (Domain-Driven Design)**: Clara separación de responsabilidades entre Modelos de Dominio, Casos de Uso (Application) e Infraestructura (Web/Persistencia).
- 🔒 **Seguridad y Autorización**:
  - JWT Stateless + Rotación de Refresh Tokens.
  - Roles (`ADMIN` / `USER`).
  - **Rate Limiting** (prevención de spamming y DDoS en capa web).
- 📦 **Control de Concurrencia**:
  - Optimistic Locking (`@Version`) integrado.
  - Previene "Race conditions" limitando la compra de stock sobrevendido al manejar transacciones críticas (Checkout simultáneo de producto agotándose).
- 📊 **Modelado y Escalabilidad**:
  - Filtros y Paginación escalable con `Pageable` y `@Query`.
  - Migraciones de de base de datos automatizadas con **Flyway**.
  - Documentación completa interactiva utilizando **Swagger OpenAPI 3**.
- 🚀 **Features Core de un E-commerce**:
  - Catálogo, Gestión de Carrito de Compras en memoria.
  - Uso iterativo de Cupones, Wishlists (favoritos).
  - Reseñas (Reviews) y sistemas de ranking promediados de productos.

---

## 📂 Arquitectura (Hexagonal)

El código se organiza estrictamente en capas:
```
com.ecommerce
├── application/     # Casos de uso (Servicios) y DTOs (Request/Response)
├── domain/          # Entidades de negocio centrales y Puertos (Repositorios Interfaces)
└── infrastructure/  # Detalles de implementación: Seguridad, Web (Controladores), Flyway
```

---

## 🛠️ Cómo Ejecutar (Local)

### 1️⃣ Usando Docker (Recomendado - Producción Config)
Incluye una base de datos PostgreSQL orquestada.
```bash
docker-compose up --build
```

### 2️⃣ Usando Maven puro (Ambiente Dev con H2 db)
La configuración de base de datos en memoria (H2) levantará y sembrará la info base (con un administrador ya existiendo).
```bash
./mvnw spring-boot:run
```

---

## 📖 Interactuando con la API interactiva (Swagger)

Una vez que la aplicación esté en marcha, navega a tu navegador y entra en: 
**`http://localhost:8080/`** (te redirigirá a `/swagger-ui.html`).

* **Credenciales de Administrador por defecto:**
  * **Email**: `admin@ecommerce.com`
  * **Password**: `admin123`

### Flujo recomendado para Swagger:
1. Ve al tag `Auth` y manda un request a `/api/auth/login`. Copia tu `accessToken`.
2. Presiona el botón verde `Authorize` (`🔒`) situado en la franja alta y pega el token.
3. Ahora puedes interactuar en vivo con `ADMIN - Productos` (CRUD), manejo de cupones, etc.
