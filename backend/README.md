# VerifiMe Backend – Invoice Calculation Service

This is a backend service built using Quarkus to calculate multi-currency invoice totals.

It exposes a REST API that converts invoice line items into a base currency using exchange rates and returns the final total.

---

## 🚀 Tech Stack

* Java 17
* Quarkus
* Maven
* Micrometer (Metrics)
* SmallRye Health (Health checks)
* OpenAPI / Swagger

---

## 📁 Project Structure

```
backend/
 ├── src/
 ├── pom.xml
 ├── Dockerfile
 └── target/
```

---

# ▶️ Running the Application

## 🟢 Option 1 — Run in Dev Mode (Local Development)

```bash
./mvnw compile quarkus:dev
```

* App runs on: http://localhost:8080
* Dev UI: http://localhost:8080/q/dev-ui

> Dev mode enables hot reload and debugging features.

---

## 🟢 Option 2 — Run in Production Mode (Jar)

### Step 1: Build

```bash
./mvnw clean package
```

### Step 2: Run

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

* Runs in production mode
* Dev UI is disabled

---

## 🐳 Option 3 — Run with Docker (Recommended for Consistency)

### Step 1: Build Docker Image

```bash
docker build -t verifime-backend .
```

### Step 2: Run Container

```bash
docker run -p 8080:8080 verifime-backend
```

### Step 3: Verify

```text
http://localhost:8080/q/health
http://localhost:8080/q/metrics
```

---

# 📡 API Endpoint

## POST /invoice/total

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

### Response example

```
1600.86
```

---

# 📊 Observability

## Metrics

```
/q/metrics
```

* HTTP request count
* Response time
* Custom metrics (if added)

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

* Dev UI is enabled only in development mode
* In production, it is disabled automatically
* Observability endpoints should be secured in real deployments

---

# 🧠 Design Highlights

* Layered architecture (Controller → Service → Client)
* Single external API call for multiple currency conversions
* Accurate financial calculations using BigDecimal
* Centralized rounding logic
* Clean error handling with proper HTTP status codes

---

# ⚠️ Notes for Developers

* Ensure port 8080 is free before running
* Use Docker for consistent environment
* Dev mode is for development only — not production use

---