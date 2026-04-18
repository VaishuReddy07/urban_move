# UrbanMove - Development & Deployment Guide

## Quick Start

### Local Development (Maven)

```bash
# Prerequisites
java -version  # Should be 17+
mvn -version   # Should be 3.8+
mysql --version # Should be 8.0+

# Setup database
mysql -u root -p
CREATE DATABASE urban_move;
USE urban_move;

# Build & run
mvn clean install
mvn spring-boot:run

# Access application
# Web: http://localhost:8080
# API Docs: http://localhost:8080/swagger-ui.html
# Login: admin@urbanmove.com / admin123
```

### Docker Deployment

```bash
# Build image
mvn clean package
docker build -t urban-move:1.0.0 .

# Run with Docker Compose
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop services
docker-compose down
```

### Kubernetes Deployment

```bash
# Build & push image
docker build -t yourregistry/urban-move:1.0.0 .
docker push yourregistry/urban-move:1.0.0

# Deploy to Kubernetes
kubectl create namespace urban-move
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/mysql-pvc.yaml
kubectl apply -f k8s/mysql-deployment.yaml
kubectl apply -f k8s/app-deployment.yaml
kubectl apply -f k8s/app-service.yaml

# Verify deployment
kubectl get pods -n urban-move
kubectl logs -f deployment/urban-move-api -n urban-move

# Port forward
kubectl port-forward -n urban-move svc/urban-move-api 8080:8080
```

## Production Deployment Checklist

### Pre-Deployment

- [ ] Run all tests: `mvn clean test`
- [ ] Check code quality: `mvn sonar:sonar`
- [ ] Review security: Run OWASP dependency check
- [ ] Load testing: Verify performance under expected load
- [ ] Database backup: Create backup of production database
- [ ] Disaster recovery: Test failover procedures

### Configuration

- [ ] Change default admin password
- [ ] Update JWT secret to strong value
- [ ] Configure database credentials
- [ ] Set up SSL/TLS certificates
- [ ] Configure email service for notifications
- [ ] Set up monitoring and alerting
- [ ] Configure log aggregation

### Deployment

```bash
# Rolling deployment with Kubernetes
kubectl set image deployment/urban-move-api \
  urban-move-api=yourregistry/urban-move:1.0.1 \
  -n urban-move --record

# Monitor rollout
kubectl rollout status deployment/urban-move-api -n urban-move

# Rollback if needed
kubectl rollout undo deployment/urban-move-api -n urban-move
```

### Post-Deployment

- [ ] Verify all services are running
- [ ] Run smoke tests against production
- [ ] Monitor application logs for errors
- [ ] Check database connectivity
- [ ] Verify backups are working
- [ ] Monitor performance metrics
- [ ] Test critical user workflows

## Performance Optimization

### Database Optimization

```sql
-- Create indexes for common queries
CREATE INDEX idx_vehicle_status ON vehicles(status);
CREATE INDEX idx_event_timestamp ON mobility_events(event_timestamp);
CREATE INDEX idx_route_vehicle ON routes(vehicle_id);

-- Analyze table statistics
ANALYZE TABLE vehicles;
ANALYZE TABLE routes;

-- Optimize tables
OPTIMIZE TABLE vehicles;
OPTIMIZE TABLE routes;
```

### Application Configuration

```properties
# Connection pooling
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000

# JPA batch processing
spring.jpa.properties.hibernate.jdbc.batch_size=10
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true

# Query optimization
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.use_sql_comments=true
```

## Monitoring & Maintenance

### Health Checks

```bash
# Application health
curl http://localhost:8080/actuator/health

# Detailed metrics
curl http://localhost:8080/actuator/metrics

# Database health
curl http://localhost:8080/actuator/health/db
```

### Backup Procedures

```bash
# Automated daily backup
0 2 * * * mysqldump -u root -p urban_move | gzip > /backups/urban_move_$(date +\%Y\%m\%d).sql.gz

# Point-in-time recovery
mysql -u root -p urban_move < /backups/urban_move_backup.sql
```

### Log Management

```bash
# View application logs
tail -f logs/urban_move.log

# Rotate logs
logrotate /etc/logrotate.d/urban_move

# Send to centralized logging
docker logs urban-move-app | tee -a /var/log/urban_move.log
```

## Scaling Strategy

### Horizontal Scaling

```yaml
# Kubernetes deployment with replicas
apiVersion: apps/v1
kind: Deployment
metadata:
  name: urban-move-api
spec:
  replicas: 3  # Scale to 3 instances
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 0
```

### Load Balancing

```yaml
# Kubernetes service with LoadBalancer
apiVersion: v1
kind: Service
metadata:
  name: urban-move-api
spec:
  type: LoadBalancer
  selector:
    app: urban-move
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8080
```

### Database Scaling

- Read replicas for reporting
- Connection pooling optimization
- Query result caching
- Database partitioning for large tables

## Troubleshooting

### Application won't start

```bash
# Check logs
docker-compose logs app

# Verify database connection
curl -X GET http://localhost:8080/actuator/health/db

# Check port availability
lsof -i :8080
```

### High memory usage

```bash
# Analyze heap dump
jmap -dump:live,format=b,file=heap.bin <PID>
jhat heap.bin

# Adjust JVM settings
-Xmx512m -Xms256m
```

### Database connectivity issues

```bash
# Verify MySQL is running
docker-compose ps mysql

# Check connection parameters
docker-compose logs mysql

# Reset database
docker-compose exec mysql mysql -u root -p -e "SHOW DATABASES;"
```

## Disaster Recovery

### Backup & Restore

```bash
# Daily backup
0 2 * * * docker-compose exec mysql \
  mysqldump -u urban_user -p urban_password urban_move | \
  gzip > /backups/urban_move_$(date +\%Y\%m\%d).sql.gz

# Restore from backup
gunzip -c /backups/urban_move_20240417.sql.gz | \
  docker-compose exec -T mysql mysql -u urban_user -p urban_password
```

### Failover Procedure

1. Detect failure: Monitor application health
2. Alert team: Send notifications
3. Failover: Route traffic to standby
4. Investigate: Determine root cause
5. Restore: Bring original back online
6. Post-incident: Review and document

## Security Hardening

### Network Security

```yaml
# Kubernetes Network Policy
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: urban-move-api
spec:
  podSelector:
    matchLabels:
      app: urban-move
  policyTypes:
  - Ingress
  - Egress
  ingress:
  - from:
    - podSelector:
        matchLabels:
          role: frontend
  egress:
  - to:
    - podSelector:
        matchLabels:
          app: mysql
```

### SSL/TLS Configuration

```properties
# Spring Boot SSL
server.ssl.key-store=classpath:keystore.jks
server.ssl.key-store-password=${KEYSTORE_PASSWORD}
server.ssl.key-store-type=JKS
```

### Secrets Management

```bash
# Kubernetes secrets
kubectl create secret generic urban-move-secrets \
  --from-literal=db-password=... \
  --from-literal=jwt-secret=... \
  -n urban-move
```

## Compliance & Audit

### Logging for Compliance

```properties
# Audit logging
logging.level.org.springframework.security=DEBUG
logging.level.com.urban.urban_move.security=DEBUG

# Include user context in logs
logging.pattern.console=%d{HH:mm:ss.SSS} %-5level [%thread] %-40logger{39} : %X{userId} %msg%n
```

### Data Retention

```sql
-- Archive old events
INSERT INTO mobility_events_archive 
SELECT * FROM mobility_events 
WHERE event_timestamp < DATE_SUB(NOW(), INTERVAL 90 DAY);

DELETE FROM mobility_events 
WHERE event_timestamp < DATE_SUB(NOW(), INTERVAL 90 DAY);
```

## Documentation References

- API Documentation: http://localhost:8080/swagger-ui.html
- Architecture Guide: [CLOUD_ARCHITECTURE.md](CLOUD_ARCHITECTURE.md)
- Implementation Details: [IMPLEMENTATION_REPORT.md](IMPLEMENTATION_REPORT.md)
- Project README: [README.md](README.md)

---

**Last Updated**: April 17, 2024
**Version**: 1.0.0
