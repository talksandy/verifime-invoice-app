# Frontend (Next.js) — VerifiMe Invoice App

This is the frontend application built using Next.js. It connects to the backend API via environment configuration.

---

## ⚙️ Prerequisites

Make sure the following are installed:

* Node.js (>= 18)
* npm (>= 9)
* Docker (for containerized run)
* Backend service running (e.g. `http://localhost:8080`)

---

## 🚀 Run Application (Local - npm)

### 1. Navigate to frontend

```bash
cd frontend
```

---

### 2. Install dependencies

```bash
npm install
```

---

### 3. Configure environment

Ensure `.env.local` exists in the `frontend/` folder with:

```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

---

### 4. Start development server

```bash
npm run dev
```

Application will be available at:

```
http://localhost:3000
```

---

## 🏗️ Run Application (Production - npm)

### 1. Build the application

```bash
npm run build
```

---

### 2. Start production server

```bash
npm start
```

---

## 🐳 Run Application (Docker)

### 1. Build Docker image

```bash
docker build -t verifime-frontend .
```

---

### 2. Run Docker container

```bash
docker run -p 3000:3000 \
  -e NEXT_PUBLIC_API_URL=http://localhost:8080 \
  verifime-frontend
```

Application will be available at:

```
http://localhost:3000
```

---

## 🔗 Backend Configuration

* Ensure backend is running before starting frontend

* For local setup:

  ```
  NEXT_PUBLIC_API_URL=http://localhost:8080
  ```

* For Docker (if backend is another container):

  ```
  NEXT_PUBLIC_API_URL=http://backend:8080
  ```

---

## ⚠️ Important Notes

* `.env.local` is used only for local development
* Always restart the app after changing environment variables
* In Docker/production, pass environment variables using `-e`

---

## 🐞 Troubleshooting

### Environment variable not detected

* Ensure `.env.local` is inside `frontend/`
* Restart the server

### App not connecting to backend

* Verify backend is running
* Check API URL

### First load is slow

* Expected due to Next.js lazy compilation

---
