# KR Creations POS — Backend

A full-stack Point of Sale (POS) system built for **KR Creations**, a graphic design business, to manage customers, services, orders, and invoicing. This repository contains the backend API, built with Spring Boot.

> Pairs with a React + TypeScript frontend (separate repository).

---

## Overview

This system replaces manual order tracking with a proper POS workflow: customers place orders for design services (social media posts, handbills, banners, cover designs, etc.), each with size/price variants, and the system generates PDF invoices automatically.

---

## Tech Stack

- **Language / Framework:** Java, Spring Boot
- **Database:** PostgreSQL
- **Authentication:** JWT (access + refresh token flow)
- **PDF Generation:** Flying Saucer + Thymeleaf (HTML → PDF invoices)
- **API Documentation:** Swagger / OpenAPI
- **Containerization:** Docker Compose
- **Build Tool:** Maven

---

## Key Features

- **JWT Authentication** — secure login with access/refresh token rotation and silent refresh support on the client side
- **Service Management** — full CRUD for design services offered by the business
- **Service Variants** — services can have multiple size/price variants (e.g. A4 vs A3 handbill pricing) via a dedicated `ServiceVariant` entity
- **Order Management** — create and track customer orders, with line-item pricing snapshotted at time of sale
- **PDF Invoice Generation** — clean, branded invoices generated per order using Thymeleaf templates rendered to PDF via Flying Saucer
- **Swagger UI** — interactive API documentation for all endpoints

---

## Key Design Decisions

A few notable architectural choices worth knowing before contributing:

- **`ServiceVariant` entity** — decouples pricing/size options from the base `Service` entity, allowing a single service to offer multiple size/price combinations without duplicating service records.
- **Snapshot pricing on `OrderItem`** — `unitPrice` is copied onto the `OrderItem` at the time of order creation rather than referenced live from `ServiceVariant`. This preserves historical accuracy — future price changes don't retroactively alter past invoices.
- **One invoice per order** — invoicing is intentionally kept simple (1:1 with orders) rather than supporting partial or batched invoicing, to match the current scale of the business.
- **Balance / credit carry-forward — deferred.** Customer running balances and credit tracking across orders are planned but not yet implemented; this is a known future phase, not an oversight.
- **`AppUser` entity naming** — the user entity is named `AppUser` rather than `User` to avoid naming collisions with PostgreSQL/Spring's own reserved terms.
- **Wrapper types over primitives** — JPA entity fields use wrapper types (`Integer`, `Long`) instead of primitives (`int`, `long`) to properly support `null` values, which primitives cannot represent (a lesson carried over from an earlier project).

---

## Getting Started

### Prerequisites

- Java 17+
- Maven
- Docker & Docker Compose
- PostgreSQL (if not using the provided Docker Compose setup)

### Running Locally

```bash
# Clone the repository
git clone <repo-url>
cd <repo-name>

# Start dependent services (PostgreSQL, etc.)
docker compose up -d

# Run the application
./mvnw spring-boot:run
```

### Environment Variables

Configure the following (e.g. via `application.yml` or a `.env` file):

| Variable | Description |
|---|---|
| `SPRING_DATASOURCE_URL` | PostgreSQL connection URL |
| `SPRING_DATASOURCE_USERNAME` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | Database password |
| `JWT_SECRET` | Secret key used to sign JWT access tokens |
| `JWT_REFRESH_SECRET` | Secret key used to sign JWT refresh tokens |

> **Note:** Never commit real secrets. Use environment-specific config files excluded via `.gitignore`.

### API Documentation

Once running, Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

---

## Project Structure

```
src/main/java/.../
├── config/          # Security, JWT, and app configuration
├── controller/       # REST controllers
├── dto/               # Request/response DTOs
├── entity/            # JPA entities (Service, ServiceVariant, Order, OrderItem, AppUser, etc.)
├── repository/        # Spring Data JPA repositories
├── service/           # Business logic
├── security/          # JWT filter, auth provider
└── templates/          # Thymeleaf templates for PDF invoice generation
```

---

## License

Private/internal project for KR Creations. Not currently licensed for public use.
