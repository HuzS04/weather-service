# Weather service

Spring Boot REST API for weather data.

## Requirements

- Java 21
- Maven

## Build

```
mvn clean verify
```

## Run

```
mvn spring-boot:run
```
App runs on port 8081

## Health check

```
GET http://localhost:8081/api/v1/health
```

Returns:
```
{"status":"UP"}
```
