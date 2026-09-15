# Finance Tracker

A REST API backend for tracking personal finances. Import transactions from a bank CSV export, have them automatically categorized by AI, and converted into your preferred currency.

Built to make it easier to document expenses, get an overview of spending, and save money.

## Features

- **User accounts** — register and log in with JWT authentication; each user only sees their own transactions
- **Transaction management** — full CRUD for income and expenses
- **CSV import** — upload a bank export and the app parses every row into transactions
- **AI categorization** — an LLM decides whether an expense is a recurring bill (Spotify, rent) or a one-off purchase, and assigns a category (food, entertainment, transport) to the one-offs
- **Currency conversion** — transactions in any currency are converted to the user's preferred currency using live exchange rates
- **Summary endpoint** — total income vs total expenses

## Tech stack

| **Layer**      | **Technology** 

| Language       | Java 25 
| Framework      | Spring Boot 4
| Security       | Spring Security, JWT (jjwt), BCrypt
| Persistence    | Spring Data JPA, Hibernate
| Database       | MySQL
| AI             | Google Gemini API
| Exchange rates | Frankfurter API
| Build          | Maven

## Architecture

The project follows a layered MVC structure:

```
Controller  →  Service  →  Repository  →  Database
```

- **Controller** — receives HTTP requests, returns JSON
- **Service** — business logic; calls out to `CategoryService` (AI) and `CurrencyService` (exchange rates) when needed
- **Repository** — database access via Spring Data JPA
- **Model** — `Transaction`, `User`, and the `TransactionType` / `Category` enums

Authentication runs as a filter before requests reach the controllers: `JwtFilter` validates the token on every request and rejects anything unauthenticated.

External integrations are isolated in their own service classes, so swapping AI or exchange-rate providers only touches one file.

## Getting started

### Prerequisites

- Java 25
- MySQL
- Maven
- A Google Gemini API key (free tier available at [aistudio.google.com](https://aistudio.google.com))

### Database setup

```sql
CREATE DATABASE financetracker;
```

Hibernate creates the tables automatically on first run.

### Configuration

Create `src/main/resources/application.properties`:

```properties
spring.application.name=Finance Tracker

spring.datasource.url=jdbc:mysql://localhost:3306/financetracker
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

gemini.api.key=YOUR_API_KEY_HERE
```

This file is gitignored — the API key never gets committed.

### Run

```bash
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`.

## API endpoints

### Auth (public)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/register` | Create an account |
| POST | `/auth/login` | Log in, returns a JWT token |

### Transactions (require `Authorization: Bearer <token>`)

| Method | Endpoint | Description |
|---|---|---|
| GET | `/transactions` | All transactions for the logged-in user |
| GET | `/transactions/{id}` | A single transaction |
| POST | `/transactions` | Create a transaction |
| PUT | `/transactions/{id}` | Update a transaction |
| DELETE | `/transactions/{id}` | Delete a transaction |
| GET | `/transactions/summary` | Total income vs total expenses |
| POST | `/transactions/import` | Upload a CSV file (form-data, key `file`) |

### Example — register

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"alex","password":"pass123","email":"alex@example.com"}'
```

### Example — log in

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"alex","password":"pass123"}'
```

Returns a JWT token, valid for 24 hours.

### Example — import a CSV

```bash
curl -X POST http://localhost:8080/transactions/import \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -F "file=@transactions.csv"
```

## CSV format

The importer expects four columns, with a header row:

```
Datum,Beskrivning,Belopp,Valuta
2026-07-01,ICA MAXI,-450.00,SEK
2026-07-02,SPOTIFY,-99.00,SEK
2026-07-03,LON,25000.00,SEK
```

- Negative amounts become expenses, positive become income
- The AI decides whether each expense is recurring or one-off
- One-off expenses get a category; recurring ones don't need one
- Amounts are converted to the user's preferred currency

## Data model

**Transaction**

| Field | Type | Notes |
|---|---|---|
| id | Long | auto-generated |
| amount | Double | stored in the user's currency |
| currency | String | the currency it was imported as |
| description | String | |
| date | LocalDate | |
| type | TransactionType | `INCOME`, `PLANNED_EXPENSE`, `DAILY_EXPENSE` |
| category | Category | only set on daily expenses |
| user | User | foreign key — the owner |

**User**

| Field | Type | Notes |
|---|---|---|
| id | Long | auto-generated |
| username | String | unique |
| password | String | BCrypt hashed, never returned in responses |
| email | String | |
| currency | String | preferred currency for converted amounts |

**Categories:** `MAT`, `RESTAURANG`, `HUSHALL`, `TRANSPORT`, `NOJE`, `HALSA`, `SPARANDE`, `OVRIGT`

## Notes and limitations

- The Gemini free tier allows 20 requests per day, which is enough for testing but not production use
- Each imported row makes up to two AI calls, so large CSV imports are slow; batching the calls would be the next optimization
- JWT signing keys are generated at startup, so tokens are invalidated whenever the app restarts

## Roadmap

- [ ] Deploy to a live environment
- [ ] Swagger / OpenAPI documentation
- [ ] Frontend
- [ ] Direct bank integration via Open Banking, replacing manual CSV upload
