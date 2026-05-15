# Resona

Веб-приложение на Spring Boot для управления музыкальным плеером в магазинах. Администраторы управляют джинглами и магазинами через браузер, а пользователи с ролью `ROLE_USER` (сущность Store) подключаются к плееру по WebSocket.

---

## Технологический стек

| Слой | Технология |
|---|---|
| Язык / рантайм | Java 25 |
| Фреймворк | Spring Boot 4.0.3 |
| Веб / MVC | Spring Web MVC, Thymeleaf |
| Безопасность | Spring Security 6 |
| Персистентность | Spring Data JPA, PostgreSQL, Flyway |
| Сессии | Spring Session + Redis (Jedis) |
| Реалтайм | Spring WebSocket (STOMP) |
| Внешние интеграции | Spring REST Client → ElevenLabs API |
| Почта | Spring Mail |
| Сборка | Gradle (с кэшированием через `gradle.properties`) |
| Локальная разработка | Docker Compose, Spring Boot DevTools |
| Контейнеризация | Docker (многослойный JAR через `bootJar { layered }`) |

---

## Архитектура и структура пакетов

```
src/main/java/kz/genvibe/
├── client/              # REST-клиенты для внешних HTTP-интеграций (ElevenLabs и др.)
├── config/              # Конфигурационные классы (@Configuration) и @ConfigurationProperties
├── controller/          # @Controller и @RestController — HTTP-обработчики
├── exception/           # Кастомные исключения и глобальный @ControllerAdvice
├── model/
│   ├── entity/          # JPA-сущности
│   ├── domain/          # DTO и доменные объекты
│   └── enums/           # Перечисления (UserRole и др.)
├── repository/          # Spring Data JPA репозитории
└── service/
    ├── internal/        # Бизнес-логика
    ├── integration/     # Сервисы для работы с внешними API
    └── util/            # Вспомогательные утилиты

src/main/resources/
├── db/migration/        # Flyway-миграции (SQL)
├── static/
│   ├── assets/          # Шрифты, иконки, медиа
│   ├── css/
│   └── js/
└── templates/
    ├── pages/           # Полные Thymeleaf-страницы
    └── fragments/       # Переиспользуемые Thymeleaf-фрагменты
```

---

## Авторизация и роли

Вход выполняется по **e-mail и паролю** (Spring Security form login). Пароли хранятся в BCrypt.

| Роль | Описание | Доступные маршруты |
|---|---|---|
| `ROLE_ADMIN` | Сотрудник / администратор | Все защищённые маршруты |
| `ROLE_USER` | Сущность «Магазин» (Store) | `/stores/{id}/{uuid}`, `/api/jingles/pause-request/{id}`, WebSocket `/ws-player/**` |

Публичные маршруты: `/auth/**`, `/onboarding/**`, `/assets/**`, `/css/**`, `/js/**`, `/users/finalize`, `/ws-player/**`, `/files/**`.

Максимум **одна активная сессия** на пользователя. При входе с нового устройства старая сессия инвалидируется (редирект на `/auth/login?expired`).

---

## Сессии в Redis

Сессии хранятся в Redis под namespace `resona:session` (Spring Session + `@EnableRedisIndexedHttpSession`). Используется клиент **Jedis** (Lettuce исключён). Это обеспечивает горизонтальное масштабирование и сохранение сессий между перезапусками приложения.

---

## WebSocket / Плеер

WebSocket-эндпоинт `/ws-player/**` используется для соединения магазина (`ROLE_USER`) с музыкальным плеером в реальном времени. CSRF для этого пути отключён (необходимо для STOMP-клиентов). Spring OXM исключён из зависимостей WebSocket.

---

## Интеграция с ElevenLabs

HTTP-клиент (`ElevenlabsClient`) создаётся через `HttpServiceProxyFactory` поверх `RestClient`. Базовый URL и API-ключ задаются через `@ConfigurationProperties` (`IntegrationProps`).

---

## Хранение файлов

Загружаемые файлы сохраняются на диск в директорию, задаваемую через `AppProps` (`fileStorage.uploadDir`). Директория маппируется как `/files/**` через `ResourceHandlerRegistry`, что позволяет отдавать файлы напрямую.

---

## Локальная разработка

### Требования

- Java 25+
- Docker & Docker Compose

### Запуск

```bash
# Поднять PostgreSQL и Redis через Docker Compose
# (spring-boot-docker-compose подхватывает compose.yml автоматически)
./gradlew bootRun
```

Spring Boot DevTools включён в `developmentOnly` — при изменении классов приложение перезагружается автоматически.

### Переменные окружения / конфигурация

Основные настройки задаются в `application.properties` (или через env-переменные):

| Свойство | Описание |
|---|---|
| `spring.datasource.*` | Подключение к PostgreSQL |
| `spring.data.redis.*` | Подключение к Redis |
| `spring.mail.*` | SMTP для отправки писем |
| `app.file-storage.upload-dir` | Директория для загружаемых файлов |
| `integration.elevenlabs.base-url` | Базовый URL ElevenLabs |
| `integration.elevenlabs.api-key` | API-ключ ElevenLabs |

---

## Сборка и Docker

```bash
# Сборка JAR
./gradlew bootJar

# Сборка Docker-образа
docker build -t resona:latest .
```

JAR собирается с **layered mode** для оптимизации кэширования слоёв Docker-образа. `developmentOnly`-зависимости (DevTools, Docker Compose support) исключены из финального артефакта.

Кэширование сборок Gradle настроено в `gradle.properties`.

---

## Миграции БД

Миграции выполняются автоматически при старте приложения через **Flyway**. SQL-файлы расположены в `src/main/resources/db/migration/` и следуют соглашению об именовании `V{version}__{description}.sql`.

---

## Тестирование

На данный момент тестовое покрытие минимально — есть тест на сохранение файлов. Тестовая инфраструктура включает:

- **Testcontainers** (PostgreSQL) — изолированные тесты с реальной БД
- Spring Boot Test Slices (`@DataJpaTest`, `@WebMvcTest` и др.)
- `spring-security-test` — тесты с аутентифицированными пользователями

```bash
./gradlew test
```

---

## Известные ограничения

- Кэширование на уровне приложения не реализовано (только сессии в Redis)
- Тестовое покрытие минимально