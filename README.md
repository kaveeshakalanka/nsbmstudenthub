# NSBM Student Hub

A Spring Boot REST API for managing student information with role-based authentication.

# Features
- Student CRUD operations (Create, Read, Update, Delete)
- User registration and login with encrypted passwords
- Role-based access control (USER, ADMIN)
- Pagination and sorting for student listings
- Input validation

# Tech Stack
- Spring Boot 4.0.2, Java 25
- MySQL Database
- Spring Security, Spring Data JPA
- Lombok, Maven

# Setup
1. Clone the repository
2. Configure MySQL in `application.properties`
3. Run: `mvn spring-boot:run`
4. Access: `http://localhost:8080`

# API Endpoints
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | /api/auth/register | Public |
| POST | /api/auth/login | Public |
| GET | /api/students | USER, ADMIN |
| POST | /api/students | ADMIN only |
| PUT | /api/students/{id} | ADMIN only |
| DELETE | /api/students/{id} | ADMIN only |
