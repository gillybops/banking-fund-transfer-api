# Banking Fund Transfer API

A production-ready REST API for secure fund transfers between bank accounts, built with Spring Boot and demonstrating enterprise-grade Java development practices.

## 🌐 Live Demo

**Production API**: https://api.gilliannewton.com  
**Interactive Documentation**: https://api.gilliannewton.com/swagger-ui.html  
**Source Code**: https://github.com/gillybops/banking-fund-transfer-api

### Authentication
- **Username**: `admin`
- **Password**: `admin`

### Quick Test
```bash
# Create an account
curl -X POST https://api.gilliannewton.com/api/v1/accounts \
  -u admin:admin \
  -H "Content-Type: application/json" \
  -d '{
    "accountHolderName": "Demo User",
    "initialBalance": 1000.00,
    "currency": "USD"
  }'

# Check all accounts
curl https://api.gilliannewton.com/api/v1/accounts -u admin:admin

# Execute a transfer (use actual account numbers from previous response)
curl -X POST https://api.gilliannewton.com/api/v1/transfers \
  -u admin:admin \
  -H "Content-Type: application/json" \
  -d '{
    "fromAccountNumber": "XXXX-XXXX-XXXX",
    "toAccountNumber": "YYYY-YYYY-YYYY",
    "amount": 250.00,
    "description": "Payment for services"
  }'
```

## 🎯 Project Overview

This API provides a complete banking fund transfer system with:
- **ACID Transaction Guarantees**: Ensures data consistency and integrity
- **Concurrent Transaction Handling**: Pessimistic locking prevents race conditions
- **Comprehensive Error Handling**: Validated inputs with meaningful error messages
- **RESTful Design**: Clean, intuitive API endpoints
- **Full Test Coverage**: Unit and integration tests included
- **API Documentation**: Interactive Swagger UI
- **Security**: Basic authentication with role-based access
- **Modern Java 25**: Built with the cutting-edge Java 25 release featuring the latest language enhancements, performance improvements, and preview features
- **Containerized Deployment**: Docker-ready with multi-stage builds
- **Production Domain**: Deployed on custom domain with automatic SSL/TLS

## 🏗️ Architecture & Design Decisions

### Key Concepts Demonstrated

1. **ACID Compliance**
   - **Atomicity**: Transactions are all-or-nothing (both debit and credit succeed or fail together)
   - **Consistency**: Business rules enforced (no negative balances)
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
   - Container-first deployment strategy

## 📋 Prerequisites

- Java 25
- Maven 3.6+
- Git
- Docker (optional, for containerized deployment)

## 🚀 Quick Start

### Local Development

```bash
# Clone the repository
git clone https://github.com/gillybops/banking-fund-transfer-api.git
cd banking-fund-transfer-api

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The API will start on `http://localhost:8080`

### Using Docker

```bash
# Build the Docker image
docker build -t banking-api .

# Run the container
docker run -p 8080:8080 banking-api
```

### Using Docker Compose

```bash
docker-compose up
```

### Quick Start Script

```bash
# Make the script executable (if not already)
chmod +x start.sh

# Run the quick start script
./start.sh
```

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

**Response:**
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

#### Get Account Details
```bash
GET /api/v1/accounts/{accountNumber}
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

**Response:**
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
- Concurrency tests for race condition prevention

## 🌐 API Documentation

### Swagger UI (Interactive)
- **Local**: http://localhost:8080/swagger-ui.html
- **Production**: https://api.gilliannewton.com/swagger-ui.html

### OpenAPI Specification
- **Local**: http://localhost:8080/api-docs
- **Production**: https://api.gilliannewton.com/api-docs

## 📊 Database

### H2 In-Memory Database (Development)
- **Access H2 Console**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:bankingdb`
- **Username**: `sa`
- **Password**: (leave blank)

### PostgreSQL (Production Ready)

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

## 🚢 Deployment

### Deploy to Render.com

The application is configured for automatic deployment to Render.com with Docker.

1. **Fork/Clone this repository**
2. **Sign up at Render.com**
3. **Create a new Web Service**:
   - Connect your GitHub repository
   - Render auto-detects the Dockerfile
   - Instance Type: Free
4. **Deploy** - Automatic builds on every push to main

### Custom Domain Setup

The production instance runs on a custom domain with automatic SSL:

1. **In Render**: Settings → Custom Domain → Add `api.yourdomain.com`
2. **In DNS Provider**: Add CNAME record pointing to Render URL
3. **Wait for SSL**: Automatic Let's Encrypt certificate provisioning

Current production domain: **api.gilliannewton.com**

### Environment Variables (Production)

```bash
# Optional: Adjust JVM memory
JAVA_TOOL_OPTIONS=-Xmx512m

# Optional: Active Spring profile
SPRING_PROFILES_ACTIVE=prod
```

## 🔒 Security Considerations

### Current Implementation (Demo/Development)
- Basic Authentication with in-memory users
- CSRF disabled for API-only access
- H2 in-memory database

### Production Recommendations
1. **Authentication**: Replace in-memory users with database-backed authentication
2. **Authorization**: Implement JWT tokens for stateless auth
3. **HTTPS**: Always use TLS/SSL (configured via Render)
4. **Rate Limiting**: Add request throttling to prevent abuse
5. **CSRF**: Enable for production if using cookies
6. **Database**: Use PostgreSQL or MySQL with proper connection pooling
7. **Secrets Management**: Use environment variables or vault services
8. **Audit Logging**: Log all transactions and authentication attempts
9. **Input Sanitization**: Already implemented via Jakarta Validation
10. **API Keys**: Consider API key authentication for service-to-service calls

## 📁 Project Structure

```
banking-fund-transfer-api/
├── src/
│   ├── main/
│   │   ├── java/com/banking/api/
│   │   │   ├── BankingApiApplication.java      # Main Spring Boot application
│   │   │   ├── config/
│   │   │   │   ├── OpenApiConfig.java          # Swagger/OpenAPI configuration
│   │   │   │   └── SecurityConfig.java         # Spring Security configuration
│   │   │   ├── controller/
│   │   │   │   ├── AccountController.java      # Account REST endpoints
│   │   │   │   └── TransferController.java     # Transfer REST endpoints
│   │   │   ├── dto/
│   │   │   │   ├── AccountDTO.java             # Account data transfer objects
│   │   │   │   └── TransferDTO.java            # Transfer data transfer objects
│   │   │   ├── exception/
│   │   │   │   ├── BankingException.java       # Custom exceptions
│   │   │   │   └── GlobalExceptionHandler.java # Centralized error handling
│   │   │   ├── model/
│   │   │   │   ├── Account.java                # Account entity
│   │   │   │   └── Transaction.java            # Transaction entity
│   │   │   ├── repository/
│   │   │   │   ├── AccountRepository.java      # Account data access
│   │   │   │   └── TransactionRepository.java  # Transaction data access
│   │   │   └── service/
│   │   │       ├── AccountService.java         # Account business logic
│   │   │       └── TransferService.java        # Transfer business logic (ACID)
│   │   └── resources/
│   │       └── application.properties          # Application configuration
│   └── test/
│       └── java/com/banking/api/
│           ├── controller/
│           │   └── BankingApiIntegrationTest.java  # Integration tests
│           └── service/
│               └── TransferServiceTest.java        # Unit tests
├── Dockerfile                                   # Multi-stage Docker build
├── docker-compose.yml                          # Docker Compose configuration
├── pom.xml                                     # Maven dependencies
├── start.sh                                    # Quick start script
└── README.md                                   # This file
```

## 💡 Key Features & Learning Outcomes

### Java 25 Features Utilized
- **Latest Language Features**: Taking advantage of the most recent Java innovations
- **Enhanced Pattern Matching**: Advanced switch expressions and pattern matching
- **Performance Optimizations**: Latest JVM improvements and garbage collection enhancements
- **Preview Features**: Access to cutting-edge Java capabilities
- **Virtual Threads**: Scalable concurrency model for high-throughput applications
- **Modern Syntax**: Leveraging the latest language improvements for cleaner code

### Spring Boot 3.3+ Features
- **Jakarta EE**: Migration from javax to jakarta namespace
- **Native Compilation**: GraalVM ready (future enhancement)
- **Observability**: Ready for Micrometer metrics

### Banking Domain Concepts
- **Double-Entry Bookkeeping**: Every transfer debits one account and credits another
- **Transaction Isolation**: Preventing dirty reads and lost updates
- **Idempotency**: Transaction IDs prevent duplicate processing
- **Audit Trail**: Complete transaction history

## 🎓 Learning Path (Python → Java)

This project demonstrates rapid learning and adaptation:

### Concepts Transferred from Python
- RESTful API design (Flask/FastAPI → Spring Boot)
- ORM patterns (SQLAlchemy → JPA/Hibernate)
- Testing discipline (pytest → JUnit)
- Dependency injection (implicit → Spring DI)

### New Concepts Mastered
- Static typing and compilation
- Annotations-based configuration
- Enterprise design patterns
- JVM ecosystem and tooling
- Maven build lifecycle

### Time Investment
- **Initial Build**: 3.5 hours (learning + implementation)
- **Deployment**: 1 hour
- **Documentation**: 30 minutes
- **Total**: ~5 hours from zero Java to production deployment

## 🤝 Contributing

This is a demonstration project, but suggestions are welcome!

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/improvement`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/improvement`)
5. Open a Pull Request

## 📄 License

Apache 2.0 - See LICENSE file for details

## 📞 Contact

**Developer**: Gillian Newton  
**Email**: gillian@gilliannewton.com

## 🙏 Acknowledgments

- Spring Boot team for excellent framework

---

**Built with ❤️ to demonstrate rapid Java learning and enterprise development skills**

*From zero Java knowledge to production-deployed API in one day*