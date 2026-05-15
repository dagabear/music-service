# Resona

A Spring Boot web application for managing in-store music playback. Administrators manage jingles and stores through a browser interface, while users with the `ROLE_USER` role (Store entities) connect to the music player via WebSocket.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language / Runtime | Java 25 |
| Framework | Spring Boot 4.0.3 |
| Web / MVC | Spring Web MVC, Thymeleaf |
| Security | Spring Security 6 |
| Persistence | Spring Data JPA, PostgreSQL, Flyway |
| Sessions | Spring Session + Redis (Jedis) |
| Real-time | Spring WebSocket (STOMP) |
| External Integrations | Spring REST Client → ElevenLabs API |
| Email | Spring Mail |
| Build | Gradle (with build caching via `gradle.properties`) |
| Local Development | Docker Compose, Spring Boot DevTools |
| Containerization | Docker (layered JAR via `bootJar { layered }`) |

---

## Architecture & Package Structure

```
src/main/java/kz/genvibe/
├── client/              # REST clients for external HTTP integrations (ElevenLabs, etc.)
├── config/              # @Configuration classes and @ConfigurationProperties
├── controller/          # @Controller and @RestController — HTTP request handlers
├── exception/           # Custom exceptions and global @ControllerAdvice
├── model/
│   ├── entity/          # JPA entities
│   ├── domain/          # DTOs and domain objects
│   └── enums/           # Enumerations (UserRole, etc.)
├── repository/          # Spring Data JPA repositories
└── service/
    ├── internal/        # Core business logic
    ├── integration/     # Services for external API communication
    └── util/            # Helper utilities

src/main/resources/
├── db/migration/        # Flyway migrations (SQL)
├── static/
│   ├── assets/          # Fonts, icons, media
│   ├── css/
│   └── js/
└── templates/
    ├── pages/           # Full Thymeleaf page templates
    └── fragments/       # Reusable Thymeleaf fragments
```

---

## Authentication & Roles

Login is performed via **email and password** (Spring Security form login). Passwords are stored as BCrypt hashes.

| Role | Description | Accessible Routes |
|---|---|---|
| `ROLE_ADMIN` | Staff / administrator | All protected routes |
| `ROLE_USER` | Store entity | `/stores/{id}/{uuid}`, `/api/jingles/pause-request/{id}`, WebSocket `/ws-player/**` |

Public routes: `/auth/**`, `/onboarding/**`, `/assets/**`, `/css/**`, `/js/**`, `/users/finalize`, `/ws-player/**`, `/files/**`.

A maximum of **one active session per user** is enforced. When logging in from a new device, the existing session is invalidated and the old client is redirected to `/auth/login?expired`.

---

## Redis Session Storage

Sessions are stored in Redis under the namespace `resona:session` using `@EnableRedisIndexedHttpSession`. The **Jedis** client is used (Lettuce is excluded). This enables horizontal scaling and session persistence across application restarts.

---

## WebSocket / Player

The WebSocket endpoint `/ws-player/**` is used to connect a store (`ROLE_USER`) to the music player in real time. CSRF protection is disabled for this path (required for STOMP clients). Spring OXM is excluded from the WebSocket dependency.

---

## ElevenLabs Integration

The `ElevenlabsClient` HTTP client is created via `HttpServiceProxyFactory` on top of `RestClient`. The base URL and API key are injected through `@ConfigurationProperties` (`IntegrationProps`).

---

## File Storage

Uploaded files are saved to disk in a directory configured via `AppProps` (`fileStorage.uploadDir`). The directory is mapped as a resource handler under `/files/**`, allowing files to be served directly by the application.

---

## Local Development

### Prerequisites

- Java 25+
- Docker & Docker Compose

### Running the Application

```bash
# PostgreSQL and Redis are started automatically via Docker Compose
# (spring-boot-docker-compose picks up compose.yml on startup)
./gradlew bootRun
```

Spring Boot DevTools is included in `developmentOnly` — the application reloads automatically on class changes.

### Configuration

Core settings are defined in `application.properties` or via environment variables:

| Property | Description |
|---|---|
| `spring.datasource.*` | PostgreSQL connection |
| `spring.data.redis.*` | Redis connection |
| `spring.mail.*` | SMTP for sending emails |
| `app.file-storage.upload-dir` | Directory for uploaded files |
| `integration.elevenlabs.base-url` | ElevenLabs base URL |
| `integration.elevenlabs.api-key` | ElevenLabs API key |

---

## Build & Docker

```bash
# Build the JAR
./gradlew bootJar

# Build the Docker image
docker build -t resona:latest .
```

The JAR is built with **layered mode** to optimize Docker image layer caching. `developmentOnly` dependencies (DevTools, Docker Compose support) are excluded from the final artifact.

Gradle build caching is configured in `gradle.properties`.

---

## Database Migrations

Migrations are applied automatically on application startup via **Flyway**. SQL files are located in `src/main/resources/db/migration/` and follow the naming convention `V{version}__{description}.sql`.

---

## Testing

Test coverage is currently minimal — there is a test for file saving. The test infrastructure includes:

- **Testcontainers** (PostgreSQL) — isolated tests against a real database
- Spring Boot Test Slices (`@DataJpaTest`, `@WebMvcTest`, etc.)
- `spring-security-test` — tests with authenticated users

```bash
./gradlew test
```

---

## Known Limitations

- No application-level caching (only session storage in Redis)
- Test coverage is minimal