# Contributing to UrbanMove

Thank you for your interest in contributing to the UrbanMove smart mobility platform! This document provides guidelines for contributing to the project.

## Code of Conduct

- Be respectful and inclusive
- Provide constructive feedback
- Focus on code and ideas, not individuals
- Report unacceptable behavior to the team

## Getting Started

1. Fork the repository
2. Clone your fork locally
3. Create a feature branch: `git checkout -b feature/your-feature-name`
4. Follow the development setup in README.md

## Development Process

### Code Standards

- Follow Google Java Style Guide
- Use meaningful variable and method names
- Keep methods small and focused (< 20 lines preferred)
- Add comments for complex logic
- Use proper exception handling

### Commit Messages

```
[FEATURE/BUG/DOCS] Short description

Detailed explanation of changes:
- What changed
- Why it changed
- Any related issues

Fixes #123
```

### Pull Request Process

1. Ensure code passes all tests: `mvn clean test`
2. Update documentation for new features
3. Add tests for new functionality (minimum 70% coverage)
4. Create detailed pull request with:
   - Description of changes
   - Related issues
   - Testing notes
   - Screenshots (if UI changes)
5. Respond to code review feedback promptly
6. Rebase onto main before merging

## Testing Requirements

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
```

### Code Coverage
```bash
mvn clean test jacoco:report
```

Target: 70%+ code coverage

## Documentation

- Update README.md for new features
- Add Javadoc comments to public APIs
- Update API documentation
- Create/update architecture decision records (ADRs)

## Reporting Bugs

### Bug Report Template

```markdown
**Description**
Clear description of the issue

**Steps to Reproduce**
1. Step 1
2. Step 2
3. Step 3

**Expected Behavior**
What should happen

**Actual Behavior**
What actually happens

**Environment**
- Java version
- Spring Boot version
- OS

**Screenshots/Logs**
Attach relevant screenshots or logs
```

## Feature Requests

### Feature Request Template

```markdown
**Title**
Brief feature title

**Description**
Detailed description of the feature

**Use Case**
How would this be used?

**Benefits**
Why is this valuable?

**Alternative Solutions**
Any alternative approaches?
```

## Code Review Guidelines

As a reviewer:
- Check for code quality and style
- Verify tests are included and passing
- Review security implications
- Ensure documentation is updated
- Provide constructive feedback

As an author:
- Be open to feedback
- Ask clarifying questions
- Make requested changes promptly
- Thank reviewers for their time

## Performance Considerations

When contributing, consider:
- Database query performance
- Memory usage
- API response times
- Frontend load times

## Security Considerations

- No hardcoded secrets or credentials
- Validate all user inputs
- Use proper authentication/authorization
- Follow OWASP guidelines
- Report security issues privately

## Release Process

1. Update version in pom.xml
2. Update CHANGELOG.md
3. Create release branch: `release/v1.0.0`
4. Create signed tag
5. Create GitHub release with notes

## Development Tools

### Required
- Java 17+
- Maven 3.8+
- Git
- MySQL 8.0+

### Recommended
- IDE: IntelliJ IDEA or VS Code
- Docker for local testing
- Postman for API testing
- DBeaver for database management

## Branch Naming

```
feature/feature-name      # New feature
bugfix/bug-description    # Bug fix
hotfix/critical-issue     # Critical production fix
docs/documentation-name   # Documentation
test/test-description     # Testing improvements
refactor/refactoring-name # Code refactoring
```

## Useful Commands

```bash
# Build project
mvn clean package

# Run locally
mvn spring-boot:run

# Run tests
mvn clean test

# Code coverage
mvn jacoco:report

# Generate documentation
mvn clean javadoc:javadoc

# Docker build
docker build -t urban-move:latest .

# Docker compose
docker-compose up -d
```

## Project Structure

```
src/main/java/com/urban/urban_move/
├── domain/        # JPA entities
├── dto/           # Data transfer objects
├── repository/    # Data access layer
├── service/       # Business logic
├── controller/    # REST & MVC controllers
├── security/      # Security configuration
├── config/        # Application configuration
└── exception/     # Custom exceptions

src/main/resources/
├── templates/     # Thymeleaf views
├── static/        # CSS, JS, images
└── db/migration/  # Flyway scripts
```

## Performance Guidelines

### Database
- Use appropriate indexes
- Batch operations when possible
- Use lazy loading for relationships
- Cache frequently accessed data

### Backend
- Use DTO pattern to minimize data transfer
- Implement pagination for large results
- Use compression for responses
- Async processing for long operations

### Frontend
- Lazy load images
- Minimize CSS/JS files
- Use CDN for static assets
- Cache API responses

## Documentation Standards

### Code Comments
```java
/**
 * Brief description of the method.
 *
 * Detailed explanation if needed.
 *
 * @param username the user's username
 * @return the user object or null if not found
 * @throws UserNotFoundException if user doesn't exist
 */
public User findByUsername(String username) {
    // Implementation
}
```

### Architecture Decision Records (ADRs)
Document significant technical decisions in `docs/adr/` with context, decision, and consequences.

## Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- [OWASP Security Guidelines](https://owasp.org/)
- [REST API Best Practices](https://restfulapi.net/)
- [Clean Code Principles](https://www.oreilly.com/library/view/clean-code-a/9780136083238/)

## Questions?

- Check existing issues and discussions
- Review documentation in README.md
- Ask in pull request comments
- Contact the team

## Recognition

Contributors will be recognized in:
- CONTRIBUTORS.md file
- Release notes
- Project website

Thank you for contributing to UrbanMove! 🚀

---

**Last Updated**: April 17, 2024
**Version**: 1.0.0
