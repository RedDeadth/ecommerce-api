# E-Commerce REST API 

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Hexagonal Architecture](https://img.shields.io/badge/Architecture-Hexagonal-8A2BE2?style=for-the-badge)

[English](#english) | [Español](#español)

---

<a name="english"></a>
## 1. English

### Overview
Production-ready REST API for an E-Commerce platform, built following modern software engineering standards. It is designed to be highly scalable, secure, and easily maintainable.

### Architecture & Project Structure
The project strictly implements Hexagonal Architecture (Domain-Driven Design).

```text
com.ecommerce
├── application/         # Application Layer (Use Cases)
│   ├── dto/             # Request/Response Data Transfer Objects
│   └── service/         # Business logic orchestration and core rules
├── domain/              # Domain Layer (Enterprise Logic)
│   ├── model/           # Core Entities (Product, Order, User, etc.)
│   └── repository/      # Repository Interfaces (Ports)
└── infrastructure/      # Infrastructure Layer (Tech Details)
    ├── persistence/     # Database seeding and initialization
    ├── security/        # JWT Filters, Rate Limiting, UserDetailsService
    └── web/             # Spring MVC REST Controllers and Exception Handlers
```

### Enterprise Features
Unlike standard tutorial applications, this backend incorporates complex enterprise-level solutions:
- **Security & Authorization**: JWT Stateless authentication with Refresh Token rotation. Role-based access control (ADMIN/USER).
- **Rate Limiting**: Custom in-memory rate limiting implementation preventing API abuse, spamming, and potential application-layer DDoS attacks.
- **Concurrency Control**: Optimistic Locking integrated into the database entities. Prevents race conditions and overselling during simultaneous checkout requests.
- **Scalability**: Scalable pagination and filtering using `Pageable` and `@Query`.
- **E-Commerce Core**: Shopping cart state management, checkout processes, coupon application with complex validation rules, rate and review system, and user wishlists.
- **CI/CD Integration**: Automated build and testing enabled via GitHub Actions.

### Deployment & Execution

#### Using Docker (Production Context)
```bash
docker-compose up --build
```

#### Using Maven (Development Context)
```bash
./mvnw spring-boot:run
```

#### API Documentation
Once the application is running, the interactive Swagger UI documentation is available at `http://localhost:8080/`. Default administrator credentials are `admin@ecommerce.com` / `admin123`.

---

<a name="español"></a>
## 2. Español

### Visión General
API REST lista para producción orientada a plataformas de E-Commerce, construida siguiendo estándares modernos de ingeniería de software. Está diseñada para ser altamente escalable, segura y fácil de mantener.

### Arquitectura y Estructura de Proyecto
El proyecto implementa estrictamente Arquitectura Hexagonal (Domain-Driven Design).

```text
com.ecommerce
├── application/         # Capa de Aplicación (Casos de Uso)
│   ├── dto/             # Objetos de Transferencia de Datos (Request/Response)
│   └── service/         # Orquestación de lógica de negocio y reglas core
├── domain/              # Capa de Dominio (Lógica Empresarial)
│   ├── model/           # Entidades Centrales (Product, Order, User, etc.)
│   └── repository/      # Interfaces de Repositorios (Puertos/Ports)
└── infrastructure/      # Capa de Infraestructura (Detalles Tecnológicos)
    ├── persistence/     # Inicialización y sembrado de base de datos
    ├── security/        # Filtros JWT, Rate Limiting, UserDetailsService
    └── web/             # Controladores REST MVC y Manejadores de Excepciones
```

### Características Empresariales
A diferencia de aplicaciones tutoriales estándar, este backend incorpora soluciones complejas de nivel empresarial:
- **Seguridad y Autorización**: Autenticación JWT Stateless con rotación de Refresh Tokens. Control de acceso basado en roles (ADMIN/USER).
- **Limitación de Peticiones (Rate Limiting)**: Implementación de limitación en memoria que previene abuso de la API, spam y posibles ataques DDoS en la capa de aplicación.
- **Control de Concurrencia**: Bloqueo Optimista (Optimistic Locking) integrado en las entidades de base de datos. Previene condiciones de carrera o sobreventa (overselling) durante peticiones simultáneas de compra.
- **Escalabilidad**: Paginación y filtrado escalables utilizando `Pageable` y `@Query`.
- **Núcleo de E-Commerce**: Gestión de estado del carrito de compras, procesos de compra (checkout), aplicación de cupones con reglas de validación complejas, sistema de calificaciones, reseñas y listas de deseos (wishlists).
- **Integración CI/CD**: Construcción y pruebas automatizadas habilitadas a través de GitHub Actions.

### Despliegue y Ejecución

#### Usando Docker (Contexto de Producción)
```bash
docker-compose up --build
```

#### Usando Maven (Contexto de Desarrollo)
```bash
./mvnw spring-boot:run
```

#### Documentación de la API
Una vez que la aplicación esté en ejecución, la documentación interactiva Swagger UI estará disponible en `http://localhost:8080/`. Las credenciales de administrador por defecto son `admin@ecommerce.com` / `admin123`.
