# 🚀 VerifiMe Invoice Application

A full-stack invoice calculation system built with:

* **Frontend:** Next.js (React + MUI)
* **Backend:** Quarkus (Java 17)
* **Deployment:** Docker & Docker Compose

The application calculates invoice totals across multiple currencies using exchange rates.

---

# 📦 Project Structure

```
verifime-invoice-app/
├── frontend/        # Next.js application
├── backend/         # Quarkus backend service
├── docker-compose.yml
├── README.md
```

---

# ⚙️ Prerequisites

Make sure you have:

* Node.js (>= 18)
* npm (>= 9)
* Java 17 (for backend local run)
* Docker (or Rancher Desktop / Docker Desktop)

---

# 🚀 Run Full Application (Recommended)

### ▶️ Using Docker Compose

```bash
docker compose up --build
```

---

## 🌐 Access Applications

* Frontend → http://localhost:3000
* Backend → http://localhost:8080

---

## 🛑 Stop Application

```bash
docker compose down
```

---

# 🧠 Architecture Overview

* Frontend (Next.js) runs in a Node.js container
* Backend (Quarkus) runs in a JVM container
* Docker Compose orchestrates both services
* Services communicate via Docker internal network
* Environment variables control API communication

---

## 🔗 Service Communication

| Environment | API URL               |
| ----------- | --------------------- |
| Local (npm) | http://localhost:8080 |
| Docker      | http://backend:8080   |

Configured using:

```env
NEXT_PUBLIC_API_URL
```

---

# 🏗️ Design Decisions

### 1. Separation of Concerns

* Frontend and backend are independent services
* Improves maintainability and scalability

---

### 2. Docker-Based Deployment

* Ensures consistent environment across machines
* Eliminates "works on my machine" issues
* Enables easy CI/CD integration

---

### 3. Environment-Based Configuration

* No hardcoded URLs
* Supports dev, staging, and production environments

---

### 4. Next.js for Frontend

* Supports SSR and modern React features
* Optimized performance with Turbopack

---

### 5. Quarkus for Backend

* Lightweight and fast Java framework
* Built-in observability (metrics + health)

---

# 🔐 Security & Best Practices

* Environment variables used for configuration
* `.env.local` is not committed to version control
* `.env.example` provided for reference
* Backend input validation implemented
* Proper HTTP status codes for error handling

---

# ⚠️ Environment Setup

## Frontend

Create file:

```
frontend/.env.local
```

Add:

```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

---

## Docker Setup

Environment variables are passed via Docker:

```bash
-e NEXT_PUBLIC_API_URL=http://backend:8080
```

---

# 🧪 Running Individually (Optional)

## ▶️ Frontend (Local)

```bash
cd frontend
npm install
npm run dev
```

---

## ▶️ Backend (Local)

```bash
cd backend
./mvnw compile quarkus:dev
```

---

# 📡 API Overview

## Endpoint

```
POST /invoice/total
```

### Example Request

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

### Example Response

```
1600.86
```

---

# ❗ Error Handling

The backend validates:

* Currency format
* Date format
* Missing/invalid fields

### Example Error

```json
{
  "error": "Currency must not be blank"
}
```

---

# 📊 Observability

* Health → http://localhost:8080/q/health
* Metrics → http://localhost:8080/q/metrics
* Swagger → http://localhost:8080/q/swagger-ui

---

# 🚀 Deployment Strategy (Summary)

* **Frontend:**

    * Vercel (recommended for SSR)
    * OR S3 + CloudFront (for static builds)

* **Backend:**

    * AWS Elastic Beanstalk (simple)
    * OR ECS + Fargate (scalable container setup)

---