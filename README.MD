# Test task (Apple): Spring Boot backend app
[![Java CI](https://github.com/andrei-punko/articles-backend-app/actions/workflows/maven.yml/badge.svg)](https://github.com/andrei-punko/articles-backend-app/actions/workflows/maven.yml)
[![Coverage](.github/badges/jacoco.svg)](https://github.com/andrei-punko/articles-backend-app/actions/workflows/maven.yml)
[![Branches](.github/badges/branches.svg)](https://github.com/andrei-punko/articles-backend-app/actions/workflows/maven.yml)

## Requirements
This should be purely a REST endpoints backend application without UI, but feel free to attach Swagger UI for convenience.

- Build tool: Maven
- DB: Postgres
- Source Control: GitHub (public)
- Application name: articles-backend-app

Application must be implemented using Spring Data (JPA/Hibernate) layer for communication with DB.

SQL queries should not be used, JPA Repositories and Entities should be used instead.

Application should have no authentication.

Application will allow to create, update and delete Articles.

Application will have `authors` entity with the following properties:
- First Name
- Last Name

**Article** will have the following properties:

Editable properties (updatable via endpoints):
- **Title** , text limited to 100 characters
- **Summary** , text limited to 255 characters
- **Text** , text with no specific limit
- **Author:** relation with the **authors** table **. ONLY** updatable at creation. Should not be updatable after Article has been created

Non-Editable properties, updated automatically by Application:
- Date Created
- Date Updated

Application should expose REST endpoints that allow the following operations:
- Create new Article
- Update existing Article
  - Only one property at the time should be updatable.
- Delete existing Article
- Retrieve all articles in the system sorted by title

The Article JSON Payload returned by endpoint(s) must contain all properties of Article listed above.

Business Logic layer of the Application must enforce the following rules:
- Non-editable properties cannot be updated/set via the create or update endpoints
- Article cannot be created or update with empty Title or Text or Author ID
- Article cannot be created with specified author id that does not exist in the **authors** table

Any business logic constraint violation or any runtime error should return an HTTP 500 error with short description.  
(Guys confirmed that 400 errors could be used instead when needed as more appropriate)

The implementation must contain adequate unit tests to cover business requirements and edge cases such as missing arguments etc.

Application must build and produce an executable jar containing all dependencies.

The **authors** table should not be updatable via endpoints. Feel free to populate it with an arbitrary data via SQL.

## Additional requirements (added by me)
- Operation for getting existing Article
- Add pagination support for `Retrieve all articles` operation
- Operations for getting authors

## Prerequisites
- Maven 3
- JDK 21

## Build an application:
```bash
mvn clean install
```
Result `jar` placed into [target](target) folder

## Build Docker image with application inside:
```bash
docker build ./ -t articles-backend-app
```

Result Docker image has name `articles-backend-app` as mentioned in command

## Start application using Maven (vs in-memory DB H2):
    mvn spring-boot:run -Dspring-boot.run.arguments="\
    --spring.datasource.url=jdbc:h2:mem:testdb \
    --spring.datasource.username=sa \
    --spring.datasource.password=password \
    --spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"

Or next way, using `dev` spring profile:

    mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=dev

## Start application by running executable jar (vs in-memory DB H2):
    java -jar target/articles-backend-app-0.0.1-SNAPSHOT.jar \
     --spring.datasource.url=jdbc:h2:mem:testdb \
     --spring.datasource.username=sa \
     --spring.datasource.password=password \
     --spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

Or next way, using `dev` spring profile:

    java -jar target/articles-backend-app-0.0.1-SNAPSHOT.jar \
     --spring.profiles.active=dev

### Start two Docker containers - with Postgres DB and application:
    docker-compose up

## Link for quick check:
http://localhost:8099/api/v1/articles

## Swagger documentation:
http://localhost:8099/swagger-ui.html

## Check logs when start without Docker:
    less ./logs/spring-boot-logger.log

## Check logs when start using Docker:
    docker exec -it articles-backend-app sh
    less /logs/spring-boot-logger.log

## Useful CURL commands

### New article addition:
```bash
curl -i -X POST -H "Accept: application/json" -H "Content-Type: application/json" -d '{ "title": "Some tittle", "text": "Some text", "author": { "id": 1 } }' http://localhost:8099/api/v1/articles
```

### Get existing article:
```bash
curl -i http://localhost:8099/api/v1/articles/1
```

### Update existing article:
```bash
curl -i -X PATCH -H "Accept: application/json" -H "Content-Type: application/json" -d '{ "title": "Another tittle" }' http://localhost:8099/api/v1/articles/2
```

### Get list of all articles:
```bash
curl -i http://localhost:8099/api/v1/articles
```

### Get list of articles with pagination support:
```bash
curl -i 'http://localhost:8099/api/v1/articles?size=2&page=4&sort=author.firstName,DESC'
```

### Deletion of article:
```bash
curl -i -X DELETE http://localhost:8099/api/v1/articles/1
```

### Get existing author:
```bash
curl -i http://localhost:8099/api/v1/authors/4
```

### Get list of all authors:
```bash
curl -i http://localhost:8099/api/v1/authors
```

## Run functional tests:

    cd func-test
    ./gradlew clean build

Check Spock report at `func-test/build/spock-reports/index.html`

## Run performance tests:
Gradle-based:

    cd load-test
    ./gradlew clean build
    java -Dlogback.configurationFile=logback-gatling.xml -jar ./build/libs/load-test-fat.jar -s=load.WebAppLoadSimulation -rf=./build/reports/gatling

Or Maven-based:

    cd load-test
    mvn clean install
    mvn gatling:test -Dlogback.configurationFile=logback-gatling.xml

Based on https://gatling.io/docs/3.0/extensions/maven_plugin

## Check Web-Sockets functionality:
Just open page http://localhost:8099 and click `Connect`.  
Check heartbeat logs in browser developer console.

## Taste ELK stack with logs providing by Filebeat:
Check [ELK README](elk-filebeat/README.MD) with details
