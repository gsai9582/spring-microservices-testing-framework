# TODO - Spring Boot Microservices Testing Framework

## Step 1: Create multi-module Maven structure
- [x] Create root `pom.xml` with modules `product-service` and `order-service`
- [x] Create root `README.md`
- [x] Create module folders for both services

## Step 2: Implement Product Service
- [ ] `product-service/pom.xml`
- [ ] Spring Boot main app
- [ ] Entity + DTOs
- [ ] Repository
- [ ] Service layer (business logic + validation/exceptions)
- [ ] REST controller
- [ ] Global exception handling (`@ControllerAdvice`)
- [ ] Unit tests: service layer (JUnit5 + Mockito)
- [ ] Unit tests: REST controllers (MockMvc + @WebMvcTest)
- [ ] Data/repository tests (@DataJpaTest with H2)
- [ ] Exception handler tests (JSON error responses)

## Step 3: Implement Order Service
- [ ] `order-service/pom.xml`
- [ ] Spring Boot main app
- [ ] Entity + DTOs
- [ ] Repository
- [ ] Product client abstraction + implementation
- [ ] Service layer (order creation + inter-service error handling)
- [ ] REST controller
- [ ] Global exception handling (`@ControllerAdvice`)
- [ ] Unit tests: service layer (JUnit5 + Mockito)
- [ ] Unit tests: REST controllers (MockMvc)
- [ ] Data/repository tests (@DataJpaTest with H2)
- [ ] Exception handler tests

## Step 4: Config + docs
- [ ] `application.properties` for each service (including H2 test config)
- [ ] Add Postman collection under `postman/`
- [ ] Add architecture/testing workflow docs (optional)

## Step 5: Verification
- [x] Run `mvn test` at root

- [ ] Ensure all tests pass

