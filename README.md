# Fabrick Backend Test - Demo Controller

This project is a Spring Boot application that acts as a wrapper for the Fabrick Sandbox APIs. It provides REST endpoints to retrieve account balance, list transactions, and execute money transfers.

## Features

- **Account Balance**: Retrieve the current balance of the configured account.
- **Transactions**: Retrieve the list of transactions for a specific date range.
    - **Persistence**: Fetched transactions are automatically saved to an in-memory H2 database.
- **Money Transfers**: Initiate a money transfer (Bonifico) and handle the specific Sandbox error response.

## Technologies

- **Java**: 21
- **Spring Boot**: 3.3.5
- **Build Tool**: Maven
- **Database**: H2 (In-memory)
- **Other**: Lombok, Spring Data JPA, Spring RestClient

## Prerequisites

- JDK 21 installed.
- Maven installed (or use the provided `mvnw` wrapper).

## Configuration

The application is configured via `src/main/resources/application.properties`.
Key configurations include:

```properties
# Fabrick API
fabrick.base-url=https://sandbox.platfr.io
fabrick.api-key=
fabrick.account-id=

# Database
spring.datasource.url=jdbc:h2:mem:testdb
```

## How to Run

1. **Clone the repository**:
   ```bash
   git clone https://github.com/flaviocnn/rest-controller-fabrick.git
   cd rest-controller-fabrick
   ```

2. **Build and Run**:
   ```bash
   mvn spring-boot:run
   ```

   The application will start on port `8080`.

## API Endpoints

### 1. Get Account Balance
**URL**: `GET /api/account/{accountId}/balance`
**Example**:
```bash
curl http://localhost:8080/api/account/14537780/balance
```

### 2. Get Transactions
**URL**: `GET /api/account/{accountId}/transactions`
**Query Params**: `fromAccountingDate` (YYYY-MM-DD), `toAccountingDate` (YYYY-MM-DD)
**Example**:
```bash
curl "http://localhost:8080/api/account/14537780/transactions?fromAccountingDate=2019-01-01&toAccountingDate=2019-12-01"
```

### 3. Create Money Transfer
**URL**: `POST /api/account/{accountId}/payments/money-transfers`
**Body**:
```json
{
  "creditorName": "John Doe",
  "creditorAccountCode": "IT1234567890123456789012",
  "description": "Payment for services",
  "currency": "EUR",
  "amount": 100.00,
  "executionDate": "2025-01-01"
}
```
**Example**:
```bash
curl -X POST http://localhost:8080/api/account/14537780/payments/money-transfers \
  -H "Content-Type: application/json" \
  -d '{
    "creditorName": "John Doe",
    "creditorAccountCode": "IT1234567890123456789012",
    "description": "Test Payment",
    "currency": "EUR",
    "amount": 100.00,
    "executionDate": "2025-01-01"
  }'
```

## Testing

Run the unit tests using Maven:

```bash
mvn test
```

## Database Console

The H2 Database console is enabled and accessible at:
- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **User**: `sa`
- **Password**: `password`
