# UrbanMove Cloud Native Smart Mobility Platform

## Overview

UrbanMove is an enterprise-grade cloud-native smart mobility management platform designed for modern urban transportation systems. It manages real-time fleet operations, vehicle tracking, route optimization, and mobility analytics within a scalable cloud architecture.

## ✨ Key Features

### 🚗 Fleet Management
- Real-time vehicle tracking with GPS
- Vehicle status management (In Service, Idle, Charging, Maintenance)
- Fuel level monitoring and alerts
- Fleet-level performance analytics
- Multi-fleet operations support

### 🗺️ Route Optimization
- Intelligent route planning with duration/distance estimation
- Real-time route progress tracking
- Route completion with actual metrics
- Historical route analytics
- Performance optimization

### 👥 User Management
- Role-based access control (Admin, Operator, Driver, Rider)
- JWT-based authentication
- Email verification and account management
- Multi-level permissions system
- Secure password management

### 📊 Real-Time Monitoring
- Live dashboard with KPI cards
- Vehicle status overview
- Fleet utilization metrics
- Activity charts and performance indicators
- System health monitoring

### 📱 Professional UI
- Responsive design with Tailwind CSS
- Modern component library
- Mobile-friendly interfaces
- Real-time data visualization
- Intuitive navigation

### 🔒 Enterprise Security
- JWT token-based authentication
- Role-based and permission-based authorization
- HTTPS/TLS encryption
- Input validation and sanitization
- Security headers configuration
- Rate limiting ready

### 📡 Cloud-Native Architecture
- Microservices-ready design
- Containerization support (Docker)
- Kubernetes deployment ready
- Horizontal scalability
- Multi-cloud deployment options

## 🏗️ Architecture

### Technology Stack

**Backend:**
- Spring Boot 4.0.5 (Java 17 LTS)
- Spring Security with JWT
- Spring Data JPA
- MySQL Database
- Flyway Migrations

**Frontend:**
- Thymeleaf Template Engine
- Tailwind CSS Framework
- Vanilla JavaScript (ES6+)
- Font Awesome Icons

**Infrastructure:**
- Docker containerization
- Kubernetes orchestration ready
- Spring Actuator for monitoring
- Prometheus metrics support

### Layered Architecture

```
┌─────────────────────────────────────┐
│    Presentation Layer               │
│  (Controllers, REST APIs, Views)    │
├─────────────────────────────────────┤
│    Business Logic Layer             │
│  (Services, Validation, Rules)      │
├─────────────────────────────────────┤
│    Data Access Layer                │
│  (Repositories, JPA)                │
├─────────────────────────────────────┤
│    Database Layer                   │
│  (MySQL with Flyway Migrations)     │
└─────────────────────────────────────┘
```

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8+
- MySQL 8.0+
- Docker (optional)
- Git

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourorg/urban-move.git
   cd urban-move
   ```

2. **Configure database:**
   ```bash
   mysql -u root -p
   CREATE DATABASE urban_move CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **Update application.properties:**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/urban_move
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

4. **Build the application:**
   ```bash
   mvn clean package
   ```

5. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

The application will be available at `http://localhost:8080`

### Docker Deployment

```bash
# Build Docker image
docker build -t urban-move:1.0.0 .

# Run container
docker run -d \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/urban_move \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=password \
  --name urban-move \
  urban-move:1.0.0
```

## 📝 API Endpoints

### Authentication
```
POST   /api/auth/register           Register new user
POST   /api/auth/login              User login
POST   /api/auth/refresh-token      Refresh JWT token
```

### Users
```
GET    /api/users/{id}              Get user details
GET    /api/users                   List all users (paginated)
PUT    /api/users/{id}              Update user profile
```

### Vehicles
```
POST   /api/vehicles                Create vehicle
GET    /api/vehicles/{id}           Get vehicle details
GET    /api/vehicles                List vehicles (with filters)
PUT    /api/vehicles/{id}/location  Update vehicle location
PUT    /api/vehicles/{id}/status    Change vehicle status
PUT    /api/vehicles/{id}/fuel      Update fuel level
```

### Routes
```
POST   /api/routes                  Create route
GET    /api/routes/{id}             Get route details
GET    /api/routes                  List routes
PUT    /api/routes/{id}/start       Start route
PUT    /api/routes/{id}/complete    Complete route
```

## 🔑 Default Credentials

**Admin User:**
- Email: `admin@urbanmove.com`
- Username: `admin`
- Password: `admin123`

⚠️ **Change these credentials immediately in production!**

## 📊 Dashboard Features

- **KPI Cards**: Real-time metrics for vehicles, routes, utilization, and system health
- **Vehicles Table**: Recent vehicles with status and fuel level
- **Quick Actions**: Add vehicle, create route, view analytics
- **System Status**: Database, API server, and cache service status
- **Activity Charts**: Weekly activity visualization
- **Performance Metrics**: On-time delivery, driver safety, fuel efficiency

## 🔐 Security Features

### Authentication
- JWT token-based stateless authentication
- 24-hour token expiration
- Automatic token refresh mechanism

### Authorization
- Role-based access control (RBAC)
- Permission-based fine-grained access
- Method-level security annotations

### Data Protection
- Bcrypt password hashing (strength 10)
- HTTPS/TLS support
- CORS configuration
- Input validation and sanitization

## 📈 Monitoring & Observability

### Actuator Endpoints
- `/actuator/health` - Application health check
- `/actuator/metrics` - Detailed metrics
- `/actuator/info` - Application information
- `/actuator/env` - Environment properties

### Logging
- File-based logging with rotation
- Structured logging support
- Different log levels (DEBUG, INFO, WARN, ERROR)
- Log location: `logs/urban_move.log`

## 📚 API Documentation

Access the interactive API documentation:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 🗄️ Database Schema

### Core Tables
- `users` - User accounts and credentials
- `roles` - User roles (Admin, Operator, Driver, Rider)
- `permissions` - Access permissions
- `vehicles` - Fleet vehicles with location/status
- `routes` - Route planning and tracking
- `mobility_events` - Real-time transportation events
- `fleets` - Fleet information and management
- `notifications` - User notifications

### Key Indexes
- `idx_user_email` - Email uniqueness
- `idx_vehicle_plate` - Plate number lookup
- `idx_vehicle_location` - Location tracking
- `idx_event_vehicle_timestamp` - Event queries
- `idx_route_vehicle_status` - Route filtering

## 🧪 Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
```

### Test Coverage
```bash
mvn clean test jacoco:report
```

## 🐳 Docker Compose Stack

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: password
      MYSQL_DATABASE: urban_move
    ports:
      - "3306:3306"
  
  app:
    build: .
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/urban_move
    ports:
      - "8080:8080"
    depends_on:
      - mysql
```

## 📋 Project Structure

```
urban-move/
├── src/
│   ├── main/
│   │   ├── java/com/urban/urban_move/
│   │   │   ├── domain/           # JPA entities
│   │   │   ├── dto/              # Data transfer objects
│   │   │   ├── repository/       # Data access layer
│   │   │   ├── service/          # Business logic
│   │   │   ├── controller/       # REST & MVC controllers
│   │   │   ├── security/         # Security configuration
│   │   │   ├── config/           # Application configuration
│   │   │   ├── exception/        # Custom exceptions
│   │   │   └── UrbanMoveApplication.java
│   │   └── resources/
│   │       ├── templates/        # Thymeleaf views
│   │       ├── static/           # CSS, JS, images
│   │       └── application.properties
│   └── test/
│       └── java/...              # Unit and integration tests
├── pom.xml                       # Maven configuration
├── Dockerfile                    # Container image
├── docker-compose.yml            # Local development stack
├── CLOUD_ARCHITECTURE.md         # Architecture documentation
├── IMPLEMENTATION_REPORT.md      # Implementation details
└── README.md                     # This file
```

## 🚦 Environment Variables

```bash
# Database
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/urban_move
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=password

# JWT
APP_JWT_SECRET=your-secret-key-here
APP_JWT_EXPIRATION=86400000  # 24 hours in milliseconds

# Server
SERVER_PORT=8080
SERVER_SERVLET_CONTEXT_PATH=/

# Logging
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_COM_URBAN_URBAN_MOVE=DEBUG
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📋 Code Standards

- Follow Google Java Style Guide
- Write meaningful commit messages
- Add tests for new features
- Document public APIs with Javadoc
- Keep methods focused and small

## 🐛 Troubleshooting

### Database Connection Issues
```bash
# Verify MySQL is running
mysql -u root -p -h localhost

# Check connection string
spring.datasource.url=jdbc:mysql://localhost:3306/urban_move
```

### Port Already in Use
```bash
# Change port in application.properties
server.port=9090
```

### JWT Token Issues
- Ensure JWT secret is properly configured
- Check token expiration time
- Verify Authorization header format: `Bearer <token>`

## 📄 License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.

## 📞 Support

- **Documentation**: See IMPLEMENTATION_REPORT.md and CLOUD_ARCHITECTURE.md
- **API Docs**: http://localhost:8080/swagger-ui.html
- **Issues**: GitHub Issues tracker
- **Email**: support@urbanmove.com

## 🎯 Roadmap

- [x] Core platform implementation
- [x] JWT authentication
- [x] REST API development
- [x] Professional UI with Tailwind CSS
- [ ] Real-time WebSocket communication
- [ ] Mobile app development
- [ ] Advanced analytics dashboard
- [ ] Machine learning integration
- [ ] Microservices refactoring
- [ ] Multi-language support

## ✅ Quality Assurance

- **Code Quality**: SonarQube analysis
- **Security**: Regular security audits
- **Performance**: Load testing and optimization
- **Reliability**: 99.9% uptime target
- **Test Coverage**: >70% code coverage

## 🎓 Learning Resources

- Spring Boot Documentation: https://spring.io/projects/spring-boot
- Tailwind CSS Documentation: https://tailwindcss.com/docs
- JWT Best Practices: https://tools.ietf.org/html/rfc7519
- REST API Design: https://restfulapi.net/

---

**Version**: 1.0.0
**Last Updated**: April 17, 2024
**Status**: Production Ready

For more detailed information, please refer to:
- [Cloud Architecture Documentation](CLOUD_ARCHITECTURE.md)
- [Implementation Report](IMPLEMENTATION_REPORT.md)
