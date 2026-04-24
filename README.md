🚀 Uber Backend Application (Monolith → Microservices Ready)
A backend system inspired by Uber, built using Spring Boot following a modular monolithic architecture, designed to be easily scalable into microservices.

🧩 Features
User & Ride Management APIs
Clean modular structure (User, Ride, Notification modules)
Secure authentication using JWT
Email notification system for ride updates (booking, status changes)
Designed with separation of concerns to support future microservice migration
⚙️ Tech Stack
Java 17
Spring Boot
REST APIs
JPA / Hibernate
MySQL / PostgreSQL
AWS (EC2, RDS)
🏗️ Architecture
This project follows a modular monolith approach:

Each module (User, Ride, Notification) is loosely coupled
Clear separation of layers (Controller → Service → Repository)
Designed in a way that each module can be extracted into a separate microservice
🔔 Notification System
Implemented an email notification service triggered on key events:

Ride booking confirmation
Ride status updates
Includes:

Event-based triggering within application
Error handling for reliable delivery
▶️ How to Run
Clone the repository
Configure application properties (database, ports)
Run the Spring Boot application
Test APIs using Postman
📌 Future Improvements
Split modules into independent microservices
Introduce API Gateway
Add messaging queue (Kafka) for async communication
Implement service discovery (Eureka)
👨‍💻 Author
Teja Koribilli Java Full Stack Developer
