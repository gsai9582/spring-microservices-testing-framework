# Spring Boot Microservices Testing Framework (JUnit 5 + Mockito + MockMvc + H2)

This project contains **two Spring Boot microservices**:
- `product-service`
- `order-service`

It demonstrates **isolated unit testing** of:
- Service layer logic (JUnit 5 + Mockito)
- REST controllers (MockMvc + @WebMvcTest)
- Repository/data handling (DataJpaTest + H2)
- Exception handling (@ControllerAdvice -> JSON responses)

## Prerequisites
- Java 17
- Maven

## Build & Test
From the repository root:
```bash
mvn test
```

Run tests for a single service:
```bash
mvn -pl product-service test
mvn -pl order-service test
```

## Notes
- Repository tests use **H2 in-memory**.
- Controller tests use **MockMvc**.
- Inter-service communication in `order-service` is mocked in unit tests.

