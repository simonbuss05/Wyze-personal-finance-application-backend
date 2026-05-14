# Wyze — Personal Finance Tracker (Backend)

A Spring Boot REST API that powers the Wyze personal finance dashboard. Integrates with the Plaid API to aggregate bank accounts and transactions across multiple financial institutions, with JWT-based authentication and automated nightly transaction syncing.

**Live API:** `https://placeholder.railway.app`  
**Frontend Repo:** [wyze-frontend](https://github.com/simonbuss05/wyze-persomal-finance-application-frontend)

---

## Tech Stack

- **Java 21** — language
- **Spring Boot 4.0** — application framework
- **Spring Security 7** — JWT authentication and route protection
- **Spring Data JPA / Hibernate** — ORM and database access
- **PostgreSQL 18** — relational database
- **Plaid Java SDK v26** — bank account and transaction aggregation
- **JJWT 0.12.5** — JWT token generation and validation
- **Maven** — dependency and build management

---

## Architecture

```
src/main/java/com/financeapp/backend/
├── controller/        # REST endpoints
│   ├── AuthController.java
│   └── PlaidController.java
├── service/           # Business logic
│   ├── FinanceUserService.java
│   └── PlaidService.java
├── entity/            # JPA entities / database tables
│   ├── FinanceUser.java
│   ├── PlaidItem.java
│   ├── PlaidAccount.java
│   └── PlaidTransaction.java
├── repository/        # Spring Data JPA repositories
├── security/          # JWT filter, security config, password encoder
│   ├── JwtUtil.java
│   ├── JwtAuthFilter.java
│   ├── SecurityConfig.java
│   ├── PasswordEncoderConfig.java
│   └── PlaidConfig.java
├── dto/               # Request/response data transfer objects
└── job/               # Scheduled background jobs
    └── PlaidSyncJob.java
```

---

## Database Schema

| Table | Description |
|---|---|
| `users` | Authenticated app users with hashed passwords |
| `plaid_items` | One row per connected bank institution per user |
| `plaid_accounts` | Individual accounts (checking, savings, credit) per institution |
| `plaid_transactions` | Transaction history across all accounts |

Foreign key relationships: `plaid_accounts` → `plaid_items` → `users`. `plaid_transactions` → `plaid_accounts` → `users`.

---

## API Endpoints

### Auth
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user, returns JWT |
| POST | `/api/auth/login` | Login with email and password, returns JWT |

### Plaid
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/plaid/create-link-token` | Generate a Plaid Link token for the frontend widget |
| POST | `/api/plaid/exchange-token` | Exchange public token for access token, sync accounts and transactions |

All Plaid endpoints require a valid JWT in the `Authorization: Bearer <token>` header.

---

## Plaid Integration Flow

```
1. Frontend calls /api/plaid/create-link-token
2. Backend calls Plaid API → returns link_token
3. Frontend opens Plaid Link widget with link_token
4. User authenticates with their bank inside Plaid's widget
5. Plaid fires onSuccess with a public_token
6. Frontend sends public_token to /api/plaid/exchange-token
7. Backend exchanges for permanent access_token → saves PlaidItem
8. Backend syncs accounts and transactions → saves to database
9. PlaidSyncJob runs nightly to keep transaction data fresh
```

---

## Key Technical Decisions

**JWT over sessions** — the API is stateless. Every request carries a signed JWT; the server never stores session state. This makes horizontal scaling trivial and fits naturally with a separate React frontend.

**Plaid cursor-based sync** — uses Plaid's `/transactions/sync` endpoint with a cursor rather than the older `/transactions/get`. The cursor is stored per PlaidItem so each sync only fetches new or modified transactions since the last run, keeping the nightly job efficient regardless of transaction volume.

**Denormalized `user_id` on transactions** — `user_id` is stored directly on `plaid_transactions` in addition to the foreign key through `plaid_accounts`. This avoids expensive joins on the most-queried table and is the standard approach for read-heavy financial data.

**BCrypt password hashing** — passwords are hashed with BCrypt before storage. Plaid credentials are never stored — Plaid handles bank authentication entirely; only the resulting access token is persisted.

---

## Running Locally

### Prerequisites
- Java 21
- PostgreSQL 18
- Maven

### Setup

**1. Clone the repository**
```bash
git clone https://github.com/simonbuss05/wyze-personal-finance-application.git
cd wyze-personal-finance-application
```

**2. Create a PostgreSQL database**
```sql
CREATE DATABASE financeapp;
CREATE USER financeuser WITH PASSWORD 'yourpassword';
GRANT ALL PRIVILEGES ON DATABASE financeapp TO financeuser;
GRANT ALL ON SCHEMA public TO financeuser;
```

**3. Configure environment**

Copy the example properties file and fill in your values:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

```properties
spring.datasource.url=jdbc:postgresql://127.0.0.1:5432/financeapp
spring.datasource.username=financeuser
spring.datasource.password=yourpassword
jwt.secret=your-base64-encoded-secret
jwt.expiration=86400000
plaid.client-id=your-plaid-client-id
plaid.secret=your-plaid-sandbox-secret
plaid.env=sandbox
server.port=8080
```

Get your Plaid credentials at [dashboard.plaid.com](https://dashboard.plaid.com).

**4. Run the application**
```bash
mvn spring-boot:run
```

The API starts on `http://localhost:8080`. Hibernate automatically creates all database tables on first run.

---

## Testing

Test the auth flow with curl or Postman:

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Simon","lastName":"Buss","email":"simon@test.com","password":"password123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"simon@test.com","password":"password123"}'
```

To test the Plaid flow in sandbox, generate a test public token directly from Plaid:

```bash
curl -X POST https://sandbox.plaid.com/sandbox/public_token/create \
  -H "Content-Type: application/json" \
  -d '{
    "client_id": "your_client_id",
    "secret": "your_sandbox_secret",
    "institution_id": "ins_3",
    "initial_products": ["transactions"]
  }'
```

---

## Deployment

Deployed on [Railway](https://railway.app). The PostgreSQL database is provisioned as a Railway plugin. All environment variables are configured in the Railway dashboard — no secrets are stored in the repository.

---

## Author

Simon Buss — [github.com/simonbuss05](https://github.com/simonbuss05)