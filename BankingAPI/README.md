# Banking Fund Transfer API

A production-ready REST API for secure fund transfers between bank accounts, built with Spring Boot and demonstrating enterprise-grade Java development practices.

## 🎯 Project Overview

This API provides a complete banking fund transfer system with:
- **ACID Transaction Guarantees**: Ensures data consistency and integrity
- **Concurrent Transaction Handling**: Pessimistic locking prevents race conditions
- **Comprehensive Error Handling**: Validated inputs with meaningful error messages
- **RESTful Design**: Clean, intuitive API endpoints
- **Full Test Coverage**: Unit and integration tests included
- **API Documentation**: Interactive Swagger UI
- **Security**: Basic authentication with role-based access
- **Modern Java 21**: Built with the latest LTS version featuring virtual threads support

## 🏗️ Architecture & Design Decisions

### Key Concepts Demonstrated

1. **ACID Compliance**
   - **Atomicity**: Transactions are all-or-nothing (both debit and credit succeed or fail together)
   - **Consistency**: Account balances never become negative; business rules are enforced
   - **Isolation**: SERIALIZABLE isolation level prevents concurrent modification issues
   - **Durability**: Changes are persisted to database immediately

2. **Concurrency Control**
   - Pessimistic locking on account records during transfers
   - Optimistic locking with version field for account updates
   - Transaction ordering to prevent deadlocks

3. **Design Patterns**
   - **DTO Pattern**: Separation of API contracts from domain models
   - **Repository Pattern**: Data access abstraction
   - **Service Layer**: Business logic encapsulation
   - **Global Exception Handler**: Centralized error handling

4. **Enterprise Best Practices**
   - Input validation using Jakarta Bean Validation
   - Proper use of HTTP status codes
   - Structured logging
   - Comprehensive API documentation

## 📋 Prerequisites

- Java 21+
- Maven 3.6+
- Git

## 🚀 Quick Start (GitHub Codespaces)

### Step 1: Create Repository and Setup Project

```bash
# Create project directory
mkdir -p BankingAPI
cd BankingAPI

# Initialize git
git init
git add .
git commit -m "Initial commit: Banking Fund Transfer API"

# Create GitHub repo (use gh CLI or web interface)
gh repo create banking-fund-transfer-api --public --source=. --remote=origin --push
```

### Step 2: Build and Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The API will start on `http://localhost:8080`

### Step 3: Access API Documentation

Open your browser and navigate to:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **API Docs**: `http://localhost:8080/api-docs`

### Step 4: Authentication

The API uses Basic Authentication. Default credentials:
- **User**: `user` / `password` (USER role)
- **Admin**: `admin` / `admin` (ADMIN role)

## 📡 API Endpoints

### Account Management

#### Create Account
```bash
POST /api/v1/accounts
Content-Type: application/json
Authorization: Basic YWRtaW46YWRtaW4=

{
  "accountHolderName": "John Doe",
  "initialBalance": 1000.00,
  "currency": "USD"
}
```

Response:
```json
{
  "id": 1,
  "accountNumber": "1234-5678-9012",
  "accountHolderName": "John Doe",
  "balance": 1000.00,
  "currency": "USD",
  "status": "ACTIVE",
  "createdAt": "2026-02-11T10:30:00"
}
```

#### Get Account Balance
```bash
GET /api/v1/accounts/{accountNumber}/balance
Authorization: Basic YWRtaW46YWRtaW4=
```

#### Get All Accounts
```bash
GET /api/v1/accounts
Authorization: Basic YWRtaW46YWRtaW4=
```

### Fund Transfers

#### Execute Transfer
```bash
POST /api/v1/transfers
Content-Type: application/json
Authorization: Basic YWRtaW46YWRtaW4=

{
  "fromAccountNumber": "1234-5678-9012",
  "toAccountNumber": "9876-5432-1098",
  "amount": 250.00,
  "description": "Payment for services"
}
```

Response:
```json
{
  "transactionId": "TXN-A1B2C3D4",
  "fromAccountNumber": "1234-5678-9012",
  "toAccountNumber": "9876-5432-1098",
  "amount": 250.00,
  "currency": "USD",
  "status": "COMPLETED",
  "description": "Payment for services",
  "timestamp": "2026-02-11T10:35:00"
}
```

#### Get Transaction Status
```bash
GET /api/v1/transfers/{transactionId}
Authorization: Basic YWRtaW46YWRtaW4=
```

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=TransferServiceTest
```

### Test Coverage
- Unit tests for service layer logic
- Integration tests for end-to-end API workflows
- Validation tests for error scenarios

## 🌐 Deployment to Your Domain

### Option 1: Deploy to Render.com (Free Tier)

1. Create a `render.yaml` in project root:
```yaml
services:
  - type: web
    name: banking-api
    env: java
    buildCommand: mvn clean install
    startCommand: java -jar target/banking-api-1.0.0.jar
    envVars:
      - key: JAVA_TOOL_OPTIONS
        value: -Xmx512m
```

2. Connect your GitHub repository to Render
3. Your API will be available at: `https://banking-api-xxxxx.onrender.com`

### Option 2: Deploy to Railway.app

1. Install Railway CLI:
```bash
npm i -g @railway/cli
```

2. Deploy:
```bash
railway login
railway init
railway up
```

3. Add custom domain in Railway dashboard

### Option 3: Deploy to AWS Elastic Beanstalk

```bash
# Install AWS EB CLI
pip install awsebcli

# Initialize EB application
eb init -p java-21 banking-api

# Create and deploy
eb create banking-api-env
eb deploy
```

### Configure Custom Domain

After deployment, point your domain to the service:
1. Add a CNAME record: `api.yourdomain.com` → `your-app-url.com`
2. Update the base URL in your demo
3. Enable HTTPS (most platforms provide free SSL)

## 📊 Database

### H2 In-Memory Database (Development)
- Access H2 Console: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:bankingdb`
- Username: `sa`
- Password: (leave blank)

### Production Database (PostgreSQL)

Update `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/banking
spring.datasource.username=postgres
spring.datasource.password=yourpassword
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

Add PostgreSQL dependency to `pom.xml`:
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
```

## 🔒 Security Considerations

### For Production Deployment:

1. **Enable HTTPS**: Always use TLS/SSL
2. **Strong Authentication**: 
   - Replace in-memory users with database-backed authentication
   - Implement JWT tokens for stateless auth
3. **Rate Limiting**: Add request throttling
4. **Enable CSRF**: Remove CSRF disable for production
5. **Input Sanitization**: Already implemented via validation
6. **Audit Logging**: Log all transactions and authentication attempts

## 🎨 Demo for Recruiter

### Prepare a Live Demo

1. **Deploy to a live URL**
2. **Create sample data**:
```bash
# Create two demo accounts
curl -X POST http://your-domain.com/api/v1/accounts \
  -u admin:admin \
  -H "Content-Type: application/json" \
  -d '{"accountHolderName":"Alice Smith","initialBalance":5000.00,"currency":"USD"}'

curl -X POST http://your-domain.com/api/v1/accounts \
  -u admin:admin \
  -H "Content-Type: application/json" \
  -d '{"accountHolderName":"Bob Johnson","initialBalance":3000.00,"currency":"USD"}'
```

3. **Demonstrate key scenarios**:
   - Successful transfer
   - Insufficient funds error
   - Invalid account error
   - Concurrent transfer handling

4. **Show Swagger UI** at `https://your-domain.com/swagger-ui.html`

### Talking Points for JP Morgan

✅ **Technical Skills Demonstrated**:
- Spring Boot framework (industry standard for microservices)
- RESTful API design following best practices
- Transaction management and ACID compliance
- Concurrent programming with pessimistic locking
- TDD with comprehensive test coverage
- API documentation with OpenAPI/Swagger
- Security implementation with Spring Security
- Database design with JPA/Hibernate
- Error handling and validation

✅ **Enterprise Concepts**:
- Microservice architecture patterns
- Clean code and SOLID principles
- Repository and DTO patterns
- Logging and monitoring readiness
- Deployment automation

✅ **Rapid Learning Demonstration**:
- Built production-grade API in <2 hours
- Understood and applied complex concepts (ACID, concurrency)
- Created deployable, testable, documented code
- Ready for real-world financial applications

## 📁 Project Structure

```
BankingAPI/
├── src/
│   ├── main/
│   │   ├── java/com/banking/api/
│   │   │   ├── BankingApiApplication.java
│   │   │   ├── config/
│   │   │   │   ├── OpenApiConfig.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── AccountController.java
│   │   │   │   └── TransferController.java
│   │   │   ├── dto/
│   │   │   │   ├── AccountDTO.java
│   │   │   │   └── TransferDTO.java
│   │   │   ├── exception/
│   │   │   │   ├── BankingException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── model/
│   │   │   │   ├── Account.java
│   │   │   │   └── Transaction.java
│   │   │   ├── repository/
│   │   │   │   ├── AccountRepository.java
│   │   │   │   └── TransactionRepository.java
│   │   │   └── service/
│   │   │       ├── AccountService.java
│   │   │       └── TransferService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/banking/api/
│           ├── controller/
│           │   └── BankingApiIntegrationTest.java
│           └── service/
│               └── TransferServiceTest.java
├── pom.xml
└── README.md
```

## 🤝 Contributing

This is a demo project, but suggestions are welcome! Open an issue or submit a PR.

## 📄 License

Apache 2.0

## 📞 Contact

For questions about this implementation, please reach out to [your-email@example.com]

---

**Built with ❤️ to demonstrate rapid Java learning and enterprise development skills**
