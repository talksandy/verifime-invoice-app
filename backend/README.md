# 🚀 VerifiMe Backend – Invoice Calculation Service

This is a backend service built using **Quarkus** that calculates multi-currency invoice totals.

It exposes a REST API that converts invoice line items into a base currency using exchange rates and returns the final total.

---

# 📦 Tech Stack

* Java 17
* Quarkus
* Maven
* Micrometer (Metrics)
* SmallRye Health (Health checks)
* OpenAPI / Swagger

---

# 📁 Project Structure

```text
backend/
 ├── src/
 ├── pom.xml
 ├── Dockerfile
 └── target/
```

---

# ⚡ Quick Start

```bash
cd backend
./mvnw compile quarkus:dev
```

App will be available at:

```
http://localhost:8080
```

---

# ▶️ Running the Application

## 🟢 Option 1 — Dev Mode (Local Development)

```bash
./mvnw compile quarkus:dev
```

* Runs on: http://localhost:8080
* Dev UI: http://localhost:8080/q/dev-ui
* Supports hot reload

---

## 🟢 Option 2 — Production Mode (Jar)

### Step 1: Build

```bash
./mvnw clean package
```

### Step 2: Run

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

* Runs in production mode
* Dev UI disabled

---

## 🐳 Option 3 — Run with Docker (Recommended)

### Step 1: Build image

```bash
docker build -t verifime-backend .
```

---

### Step 2: Run container

```bash
docker run -p 8080:8080 verifime-backend
```

---

### Step 3: Verify

* Health → http://localhost:8080/q/health
* Metrics → http://localhost:8080/q/metrics

---

## ⚙️ Docker Configuration

Environment variables can be passed at runtime:

```bash
docker run -p 8080:8080 \
  -e EXTERNAL_API_URL=<api-url> \
  verifime-backend
```

---

# 📡 API Endpoint

## POST `/invoice/total`

### Request Example

```json
{
  "invoice": {
    "currency": "NZD",
    "date": "2020-07-07",
    "lines": [
      {
        "description": "CPU",
        "currency": "USD",
        "amount": 700
      }
    ]
  }
}
```

---

### Response Example

```json
{
  "total": 1600.86
}
```

---

# ❗ Error Handling

The API validates input and returns meaningful error responses.

### Example: Invalid Request

```json
{
  "invoice": {
    "currency": "",
    "date": "2020-07-07",
    "lines": []
  }
}
```

### Response

```json
{
  "error": "Currency must not be blank"
}
```

---

# ✅ Validation Rules

* Currency must be a valid ISO currency code
* Date must be in ISO format (`YYYY-MM-DD`)
* Amount must be positive
* Invoice lines must not be empty

---

# 🔄 Request Flow

1. Client sends invoice request
2. Controller validates input
3. Service processes invoice data
4. External API is called for exchange rates
5. Total amount is calculated
6. Response is returned to client

---

# 🔄 External API Handling

* Exchange rates are fetched from an external service
* Retry mechanism is implemented for transient failures
* Graceful error handling if the external API is unavailable

---

# 🌐 CORS Configuration

* Backend allows requests from frontend origin (e.g. `http://localhost:3000`)
* Configurable via `application.properties`

---

# 📊 Observability

## Metrics

```
/q/metrics
```

* HTTP request count
* Response time
* Custom metrics

---

## Health Checks

```
/q/health
```

* Application health status
* Readiness checks

---

# 📘 API Documentation

Swagger UI:

```
http://localhost:8080/q/swagger-ui
```

---

# 🔐 Security Notes

* Dev UI enabled only in development mode
* Disabled in production
* Observability endpoints should be secured in real deployments
* Input validation prevents malformed requests

---

# ⚠️ Notes for Developers

* Ensure port 8080 is free before running
* Use Docker for consistent environment
* Dev mode is for development only

---

# 🧠 Design Highlights

* Layered architecture (Controller → Service → Client)
* Single external API call for multiple currency conversions
* Accurate financial calculations using `BigDecimal`
* Centralized rounding logic
* Clean and consistent error handling

---