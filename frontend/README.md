# 🚀 Frontend (Next.js) — VerifiMe Invoice App

This is the frontend application built using **Next.js**. It interacts with the backend API to calculate invoice totals and display results to the user.

---

# ⚡ Quick Start

```bash
cd frontend
npm install
npm run dev
```

App will be available at:

```
http://localhost:3000
```

---

# 📦 Prerequisites

Make sure you have:

* Node.js (>= 18)
* npm (>= 9)
* Docker (optional, for containerized run)
* Backend service running (default: `http://localhost:8080`)

---

# ⚙️ Environment Setup

Create a `.env.local` file from the example:

```bash
cp .env.example .env.local
```

Update the API URL if needed:

```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

> ⚠️ `.env.local` is not committed to version control.

---

# 🔄 Frontend–Backend Communication

The frontend communicates with the backend using:

```
NEXT_PUBLIC_API_URL
```

### Environment-specific values:

| Environment | API URL               |
| ----------- | --------------------- |
| Local (npm) | http://localhost:8080 |
| Docker      | http://backend:8080   |

### Why different values?

* `localhost` → refers to your machine
* `backend` → refers to Docker service name inside container network

---

# 🚀 Running the Application

## 🟢 Local Development (Recommended)

### 1. Navigate to frontend

```bash
cd frontend
```

### 2. Install dependencies

```bash
npm install
```

### 3. Start development server

```bash
npm run dev
```

* Uses Turbopack (fast dev server)
* Compiles pages on-demand

---

## 🏗️ Production Mode (Local)

### 1. Build the application

```bash
npm run build
```

### 2. Start production server

```bash
npm start
```

* Optimized build
* No hot reload

---

# 🐳 Running with Docker

## 1. Build Docker image

```bash
docker build -t verifime-frontend .
```

---

## 2. Run container

```bash
docker run -p 3000:3000 \
  -e NEXT_PUBLIC_API_URL=http://localhost:8080 \
  verifime-frontend
```

App will be available at:

```
http://localhost:3000
```

---

## ⚠️ Important (Docker Networking)

If backend is also running in Docker:

```bash
-e NEXT_PUBLIC_API_URL=http://backend:8080
```

👉 `localhost` inside Docker refers to the container itself, NOT your machine.

---

# ⚡ Next.js Behavior

### Development Mode (`npm run dev`)

* Uses Turbopack
* Lazy compilation (pages compile on first access)
* Faster rebuilds

### Production Mode

* Uses pre-built assets (`next build`)
* Optimized performance
* Suitable for deployment

---

# 🔗 Backend Configuration

Ensure backend is running before starting frontend.

### Default:

```
http://localhost:8080
```

---

# ⚠️ Important Notes

* Restart the app after changing environment variables
* `.env.local` is used only for local development
* Use environment variables in production (Docker / hosting platform)
* Do not commit `.env.local`

---

# 🐞 Troubleshooting

### ❌ Environment variable not detected

* Ensure `.env.local` exists in `frontend/`
* Restart dev server

---

### ❌ App cannot connect to backend

* Verify backend is running
* Check `NEXT_PUBLIC_API_URL`
* Ensure correct URL for Docker vs local

---

### ❌ CORS errors

* Ensure backend CORS configuration allows `http://localhost:3000`
* Verify backend is returning correct headers

---

### ❌ Slow first load

* Expected behavior due to Next.js lazy compilation
* Subsequent loads will be faster

---

# 🧠 Key Highlights

* Uses modern Next.js features (SSR-ready)
* Environment-based configuration (no hardcoded URLs)
* Docker support for consistent environments
* Clean separation from backend service

---
