# first_foodo — Food Delivery Backend (Zomato-style)

A REST API backend for a food-delivery platform, built with **Spring Boot 3** and **Java 21**. The project's main focus was building a real **JWT-based authentication and authorization system from scratch** on top of Spring Security — including access/refresh tokens, role-based access control, and a custom `UserDetailsService`/`PasswordEncoder` — rather than relying on a pre-built auth starter.

## Tech stack

- **Java 21**, **Spring Boot 3.5.3**
- **Spring Security** — stateless, JWT-based auth
- **Spring Data JPA** / **Hibernate** — ORM, `ddl-auto=update` schema management
- **MySQL 8**
- **JJWT (io.jsonwebtoken)** — JWT generation/validation
- **ModelMapper** — entity ↔ DTO conversion
- **Lombok** — boilerplate reduction
- **Jakarta Bean Validation** — request payload validation
- **Maven** (with wrapper — no local Maven install required)

## Features

- **JWT authentication** with separate short-lived **access tokens** and longer-lived **refresh tokens**, plus a `/refresh-token` endpoint to renew access without re-login
- **Role-based authorization** (`ADMIN` / `GUEST`) enforced at the endpoint level via Spring Security's `requestMatchers`
- **User management** — registration, lookup, listing
- **Restaurant management** — full CRUD, admin-only for create/update/delete, public read access
- **Paginated & sortable restaurant listing** (`page`, `size`, `sortBy`, `sortDir` query params)
- **Restaurant banner image upload**, restricted to `.jpg/.jpeg/.png/.webp` by both content-type and extension
- **Centralized exception handling** via `@RestControllerAdvice`, returning consistent JSON error responses instead of raw stack traces
- **Request validation** on all write endpoints (`@Valid` + Bean Validation constraints), with field-level error messages

## Auth flow

1. `POST /api/v1/auth/login` with email/password → returns an `accessToken`, a `refreshToken`, and the user's profile
2. Subsequent requests send `Authorization: Bearer <accessToken>`; a custom `JwtAuthenticationFilter` validates the token per-request and populates the Spring Security context
3. When the access token expires, `POST /api/v1/auth/refresh-token` with the `refreshToken` issues a new token pair — no re-login needed
4. New users are auto-assigned the `GUEST` role on registration (seeded via `data.sql`); `ADMIN`-only endpoints are protected by `hasRole(...)` checks against the roles attached to the authenticated user

## API endpoints

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/v1/auth/login` | Public | Authenticate, receive access + refresh tokens |
| `POST` | `/api/v1/auth/refresh-token` | Public | Exchange a valid refresh token for a new token pair |
| `POST` | `/api/v1/users` | Public | Register a new user (assigned `GUEST` role) |
| `GET` | `/api/v1/users` | Authenticated | List all users |
| `GET` | `/api/v1/users/{userId}` | Authenticated | Get a user by ID |
| `DELETE` | `/api/v1/users/{userId}` | `ADMIN` | Delete a user |
| `GET` | `/api/v1/restaurants` | Public | Paginated, sortable list of restaurants |
| `POST` | `/api/v1/restaurants` | `ADMIN` | Create a restaurant |
| `PUT` | `/api/v1/restaurants` | `ADMIN` | Update a restaurant |
| `DELETE` | `/api/v1/restaurants/{restaurantId}` | `ADMIN` | Delete a restaurant |
| `POST` | `/api/v1/restaurants/image/{restaurantId}` | `ADMIN` | Upload a restaurant's banner image |

**Example — register a user:**
```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","password":"secret123","phoneNumber":"9876543210"}'
```

**Example — login:**
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"secret123"}'
```

**Example — authenticated request:**
```bash
curl http://localhost:8080/api/v1/users \
  -H "Authorization: Bearer <accessToken>"
```

## Getting started locally

**Prerequisites:** Java 21, MySQL running locally, an IDE (IntelliJ recommended — Maven is bundled via `./mvnw`, no separate install required).

1. **Create the database:**
   ```sql
   CREATE DATABASE food_delivery_app;
   ```
2. **Configure the connection** in `src/main/resources/application.properties` (defaults to `root`/local MySQL on `localhost:3306`) — update the credentials to match your local MySQL setup.
3. **Run it:**
   ```bash
   ./mvnw spring-boot:run
   ```
   Hibernate creates the schema automatically (`ddl-auto=update`), and `data.sql` seeds the `ADMIN`/`GUEST` roles needed for user registration to work.
4. The API is available at `http://localhost:8080`.

## Project structure

```
src/main/java/com/first/foodo/first_foodo/
├── Config/          # App-wide beans (ModelMapper) and constants
├── Controllers/      # REST endpoints
├── Dto/              # Request/response payloads
├── Entity/           # JPA entities
├── Exception/         # Custom exceptions + global exception handler
├── Repository/       # Spring Data JPA repositories
├── Security/          # JWT filter/service, Spring Security config, UserDetailsService
├── Service/           # Business logic interfaces + implementations
└── Utils/             # Small shared helpers
```

## Notes on design choices

- **Stateless auth**: no server-side session; every request is authenticated independently via the JWT in the `Authorization` header, which is what makes the refresh-token flow necessary in the first place.
- **DTOs everywhere**: controllers never expose JPA entities directly, keeping the persistence model decoupled from the API contract. Sensitive fields (like password hashes) are excluded from outbound JSON via Jackson's `WRITE_ONLY` access.
- **This is a learning/portfolio project** — a few things are simplified compared to a production setup (e.g. DB credentials live in `application.properties` for local-dev convenience rather than environment variables/secrets management).
