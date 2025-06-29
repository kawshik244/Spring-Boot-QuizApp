# 🎯 Spring Boot QuizApp

A backend-only quiz application built with **Spring Boot**, designed for creating, managing, and taking quizzes. It follows enterprise-level best practices including DTO separation, centralized exception handling, layered architecture, and comprehensive testing.

---

## 📚 Table of Contents

1. [Features](#-features)  
2. [Tech Stack](#-tech-stack)  
3. [Project Structure](#-project-structure)  
4. [API Endpoints](#-api-endpoints)  
5. [Database Schema](#-database-schema)  
6. [Testing Strategy](#-testing-strategy)  
7. [Running the Project](#-running-the-project)  
8. [Future Enhancements](#-future-enhancements)  
9. [Repository](#-repository)

---

## ✅ Features

- Add, update, delete quiz questions  
- Fetch questions by category and difficulty  
- Dynamic quiz creation (by category & count)  
- Submit quiz and calculate results  
- Validation with Jakarta Bean Validation (`@Valid`)  
- Centralized exception handling using `@ControllerAdvice`  
- Clean model/controller/service/DAO layers with DTOs & BaseEntity  
- Timestamps & audit fields (`createdAt`, `updatedAt`, `createdBy`, `updatedBy`)  
- Full test coverage: Controller, Service, Repository layers

---

## 🛠 Tech Stack

- **Java** 21  
- **Spring Boot** 3.x  
- **PostgreSQL** (for production)  
- In-memory **H2** (for tests)  
- **Spring Data JPA**  
- **Jakarta Validation** (`@NotNull`, `@NotBlank`, etc.)  
- **Maven** build tool  
- **Lombok** for boilerplate reduction  
- **JUnit 5**, **Mockito**, **MockMvc** for testing  
- **Jackson** for JSON processing

---

## 🧱 Project Structure

src/main/java/com/kawshik/quizApp/
├── controller/
│ ├── QuestionController.java
│ └── QuizController.java
├── service/
│ ├── QuestionService.java
│ ├── QuizService.java
│ └── impl/
│ ├── QuestionServiceImpl.java
│ └── QuizServiceImpl.java
├── dto/
│ ├── QuestionRequestDTO.java
│ ├── QuestionResponseDTO.java
│ ├── QuizRequestDTO.java
│ └── QuizResponseDTO.java
├── dao/
│ ├── QuestionDao.java
│ └── QuizDao.java
├── model/
│ ├── BaseEntity.java
│ ├── Question.java
│ └── Quiz.java
└── exception/
├── GlobalExceptionHandler.java
└── ResourceNotFoundException.java

src/test/java/com/kawshik/quizApp/
├── controller/ # MockMvc + @WebMvcTest
├── service/ # @InjectMocks + Mockito
└── dao/ # @DataJpaTest with H2


---

## 🔗 API Endpoints

### Question APIs (`/api/v1/questions`)
| Method | Endpoint             | Description                           |
|--------|----------------------|---------------------------------------|
| GET    | `/`                   | Fetch all questions                   |
| GET    | `/category/{category}`| Fetch questions by category          |
| POST   | `/`                   | Add a new question                    |
| PUT    | `/{id}`               | Update question by ID                 |
| DELETE | `/{id}`               | Delete question by ID                 |

### Quiz APIs (`/api/v1/quiz`)
| Method | Endpoint        | Description                                        |
|--------|-----------------|----------------------------------------------------|
| POST   | `/create`        | Create a quiz by category and number of questions |
| GET    | `/{quizId}`      | Fetch questions of a quiz by its ID               |
| POST   | `/submit`        | Submit quiz responses and get result              |

---

## 🗃️ Database Schema

**`BaseEntity`** (common parent for all entities):
- `createdAt`, `updatedAt`, `createdBy`, `updatedBy`  
  (via `@MappedSuperclass`, with `@PrePersist`/`@PreUpdate`)

**`Question`** table:
- `id`, `questionTitle`, `option1-4`, `rightAnswer`, `category`, `difficultyLevel`, timestamps, audit fields

**`Quiz`** table:
- `id`, `title`, many-to-many with `Question`, timestamps, audit fields

---

## 🧪 Testing Strategy

### Controller Layer  
- `@WebMvcTest` + `MockMvc` + `@MockBean`  
- Tests like `shouldCreateQuestionSuccessfully()` and validation failure scenarios

### Service Layer  
- `@InjectMocks` + `@Mock` for DAOs  
- Tests ensure business logic and exception throwing

### Repository Layer  
- `@DataJpaTest` with H2  
- Tests for `findByCategory()` and using real JPA queries

---

## ▶️ Running the Project

1. Clone the repo:
   ```bash
   git clone https://github.com/kawshik244/Spring-Boot-QuizApp.git
   cd Spring-Boot-QuizApp
2. Configure PostgreSQL in application.properties:
   ```bash
   spring.datasource.url=jdbc:postgresql://localhost:5432/quizapp
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
3. Run the app:
   ```bash
   mvn clean spring-boot:run
4. Test APIs via Postman using base URL:
   ```bash
   http://localhost:8080/api/v1/questions
   http://localhost:8080/api/v1/quiz
5. Run tests:
   ```bash
   mvn test

---

## 🔭 Future Enhancements
  🎯 Add authentication and role-based access (Spring Security)

  🚀 Separate Admin & User modules

  📘 Document APIs with Swagger/OpenAPI

  🗄 Save quiz results and user stats

  🐳 Dockerize for cloud deployment

