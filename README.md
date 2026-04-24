# 🚀 Uber Backend Application (Modular Monolith → Microservices Ready)

A backend system inspired by Uber, built using **Spring Boot**.  
Designed with a **modular monolithic architecture** that can be easily scaled into microservices in the future.

---

## 🧩 Features

- 👤 User & Ride Management APIs  
- 🏗️ Clean modular structure (User, Ride, Notification modules)  
- 🔐 JWT-based authentication  
- 📩 Email notification system for ride updates  
- ⚡ Event-driven internal communication  
- 🧱 Designed for easy microservice migration  

---

## ⚙️ Tech Stack

- ☕ Java 17  
- 🌱 Spring Boot  
- 🔗 REST APIs  
- 🗄️ Spring Data JPA / Hibernate  
- 🐬 MySQL / PostgreSQL  
- ☁️ AWS (EC2, RDS)

---

## 🏗️ Architecture

This project follows a **modular monolith design**:

- Each module (User, Ride, Notification) is independent  
- Clear layering: `Controller → Service → Repository`  
- Low coupling between modules  
- Each module can be extracted into a microservice later  

---

## 🔔 Notification System

Email notifications are triggered on key events:

- Ride booking confirmation  
- Ride status updates  

Includes:
- Internal event-based triggers  
- Reliable email delivery handling  

---

## ▶️ How to Run

```bash
# Clone repo
git clone https://github.com/tejaKoribilli/my-projects.git

# Move into project
cd my-projects

# Run Spring Boot app
./mvnw spring-boot:run
