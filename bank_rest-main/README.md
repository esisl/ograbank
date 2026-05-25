# Bank Cards REST API

RESTful-сервис для управления банковскими картами с аутентификацией через JWT.

## 🚀 Быстрый старт

### Требования
- Java 17+
- PostgreSQL 15+
- Maven 3.8+

### 1. Подготовка БД
```sql
CREATE DATABASE bank_db;
CREATE USER bank_user WITH PASSWORD 'bank_pass';
GRANT ALL PRIVILEGES ON DATABASE bank_db TO bank_user;
\c bank_db
GRANT ALL ON SCHEMA public TO bank_user;
```

### 2. Конфигурация
Создайте `src/main/resources/application-dev.yaml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/bank_db
    username: bank_user
    password: bank_pass
  jpa:
    hibernate:
      ddl-auto: none
  liquibase:
    enabled: true
    change-log: classpath:db/migration/changelog-master.xml

jwt:
  secret: "change-me-in-production-min-32-chars"
  expiration: 3600000
```

### 3. Запуск
```cmd
mvn clean compile
mvn spring-boot:run
```
Приложение доступно на `http://localhost:8080`.

### 4. Тестирование
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI spec: `http://localhost:8080/v3/api-docs`

## 🔐 Аутентификация

1. Регистрация:
```http
POST /auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "SecurePass123"
}
```

2. Вход (получение токена):
```http
POST /auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "SecurePass123"
}
```

3. Использование токена:
```http
GET /api/cards/user/{userId}
Authorization: Bearer <your_jwt_token>
```

## 📦 Эндпоинты

| Метод | Путь | Описание | Доступ |
|-------|------|----------|--------|
| `POST` | `/auth/register` | Регистрация пользователя | Публичный |
| `POST` | `/auth/login` | Получение JWT | Публичный |
| `GET` | `/auth/me` | Информация о текущем пользователе | Аутентифицированный |
| `POST` | `/api/cards` | Создание карты | `USER`/`ADMIN` |
| `GET` | `/api/cards/user/{id}` | Список карт пользователя | `USER`(свои)/`ADMIN`(все) |
| `POST` | `/api/cards/transfer` | Перевод между своими картами | `USER`/`ADMIN` |

### Параметры пагинации
Для `GET /api/cards/user/{id}`:
- `page` (default: 0) — номер страницы
- `size` (default: 10, max: 100) — элементов на странице
- `sortBy` (default: createdAt) — поле сортировки
- `direction` (default: DESC) — `ASC` или `DESC`

Пример: `?page=0&size=5&sortBy=balance&direction=ASC`

## 🔧 Разработка

### Структура проекта
```
src/
├── main/
│   ├── java/com/example/bankcards/
│   │   ├── controller/  # REST-контроллеры
│   │   ├── service/     # Бизнес-логика
│   │   ├── repository/  # JPA-репозитории
│   │   ├── entity/      # JPA-сущности
│   │   ├── dto/         # Data Transfer Objects
│   │   ├── security/    # JWT, UserDetails, SecurityConfig
│   │   ├── util/        # JwtUtil
│   │   └── exception/   # GlobalExceptionHandler
│   └── resources/
│       ├── db/migration/ # Liquibase-миграции
│       └── application*.yaml
```

### Тестирование через PowerShell
```powershell
# Логин
$token = (Invoke-RestMethod -Uri "http://localhost:8080/auth/login" -Method Post -ContentType "application/json" -Body (@{email="user@example.com";password="SecurePass123"}|ConvertTo-Json -Compress)).accessToken
$headers = @{ Authorization = "Bearer $token" }

# Создание карты
$card = Invoke-RestMethod -Uri "http://localhost:8080/api/cards" -Method Post -ContentType "application/json" -Body '{"userId":"...","cardNumber":"4111111111111111","ownerName":"Test","expiryDate":"2030-01-01"}' -Headers $headers

# Перевод
$transfer = '{"fromCardId":"...","toCardId":"...","amount":10.00}'
Invoke-RestMethod -Uri "http://localhost:8080/api/cards/transfer" -Method Post -ContentType "application/json" -Body $transfer -Headers $headers
```

> ⚠️ В PowerShell используйте одинарные кавычки для query string: `?page=0&size=10` → `'?page=0&size=10'`

## 🔒 Безопасность

- Пароли хешируются через BCrypt
- JWT подписывается HS256, срок жизни — 1 час
- Все эндпоинты `/api/**` требуют аутентификации
- Переводы возможны только между картами одного владельца
