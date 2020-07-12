# Spring boot reactive application for the number generation service

## Frameworks

- **Spring Boot**
- **Spring Data- Reactive Mongo**
- **Webflux**

## Setup

- Maven
- Java 1.8
- Mongo
- Docker

### Maven

```
docker-compose -f docker-compose-mongo.yml up

mvn clean install 

docker-compose -f docker-compose-mongo.yml down
 
```

### Run

```
docker-compose up  --no-deps --build

Use Swagger UI for the APIs requests
  "http://localhost:9090/swagger-ui.html"

docker-compose down 
```

### Stop
```
docker-compose down 
```

