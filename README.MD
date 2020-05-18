# Implement a Spring Boot Backend application (no UI)

This should be purely a REST endpoints backend application without UI, but feel free to attach Swagger UI for convenience.

_Build tool_: Maven

_DB_: Postgres

_Source Control:_ Git Hub (public)

_Application name:_ articles-backend-app

Application must be implemented using Spring Data (JPA/Hibernate) layer for communication with DB.

SQL queries should not be used, JPA Repositories and Entities should be used instead.

Application should have no authentication.

Application will allow to create, update and delete Articles.

Application will have &quot; **authors**&quot; entity with the following properties:

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
- Article cannot be created or update with empty Title or Text or Author Id
- Article cannot be created with specified author id that does not exist in the **authors** table

Any business logic constraint violation or any runtime error should return a HTTP 500 error with short description.  
(Guys confirmed that 400 errors could be used instead when needed as more appropriate)

The implementation must contain adequate unit tests to cover business requirements and edge cases such as missing arguments etc.

Application must build and produce an executable jar containing all dependencies.

The **authors** table should not be updatable via endpoints. Feel free to populate it with an arbitrary data via SQL.

## Additional functional
- Operation for getting existing Article
- Add pagination support for 'Retrieve all articles' operation
- Operations for getting authors
- Apply role-based security restrictions using basic authorization (encrypted credentials stored in DB): two roles - 
ADMIN and USER, and USER has only read permissions

## Build instructions

#### Build application:
    mvn clean install

#### Build Docker image with application inside:
    docker build ./ -t articles-backend-app

## Usage description

#### Start application using Maven (vs in-memory H2 DB):
    mvn spring-boot:run -Dspring-boot.run.arguments=\
    --spring.datasource.url=jdbc:h2:mem:testdb,\
    --spring.datasource.username=sa,\
    --spring.datasource.password=password,\
    --spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

Or next way, using `dev` spring profile:

    mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=dev

#### Start application by running executable jar (vs in-memory H2 DB):
    java -jar target/articles-backend-app-0.0.1-SNAPSHOT.jar \
     --spring.datasource.url=jdbc:h2:mem:testdb \
     --spring.datasource.username=sa \
     --spring.datasource.password=password \
     --spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

Or next way, using `dev` spring profile:

    java -jar target/articles-backend-app-0.0.1-SNAPSHOT.jar \
     --spring.profiles.active=dev

#### Start two Docker containers - one with Postgres DB and another with application:
    docker-compose up

#### Swagger documentation situated here:
    http://localhost:8099/swagger-ui.html

#### New article addition:
    curl -i -u Ivan:ivan_pass -H "Accept: application/json" -H "Content-Type: application/json" -d '{ "title": "Some tittle", "text": "Some text", "author": { "id": 1 } }' -X POST http://localhost:8099/articles

#### Get existing article:
    curl -i -u Vasily:vasily_pass http://localhost:8099/articles/1

#### Update existing article:
    curl -i -u Ivan:ivan_pass -H "Accept: application/json" -H "Content-Type: application/json" -d '{ "title": "Another tittle" }' -X PATCH http://localhost:8099/articles/2

#### Get list of all articles:
    curl -i -u Vasily:vasily_pass http://localhost:8099/articles

#### Get list of articles with pagination support:
    curl -i -u Vasily:vasily_pass 'http://localhost:8099/articles?size=2&page=4&sort=author.firstName,DESC'

#### Deletion of article:
    curl -i -u Ivan:ivan_pass -X DELETE http://localhost:8099/articles/1

#### Get existing author:
    curl -i -u Vasily:vasily_pass http://localhost:8099/authors/4

#### Get list of all authors:
    curl -i -u Vasily:vasily_pass http://localhost:8099/authors

## To run functional tests:

    cd func-test
    ./gradlew build

Check Spock report at `func-test/build/spock-reports/index.html`

## To run performance tests:

Gradle-based:

    cd load-test
    ./gradlew build
    java -Dlogback.configurationFile=logback-gatling.xml -jar ./build/libs/load-test-fat.jar -s=load.WebAppLoadSimulation -rf=./build/reports/gatling

Or Maven-based:

    cd load-test
    mvn clean install
    mvn gatling:test -Dlogback.configurationFile=logback-gatling.xml

Based on https://gatling.io/docs/3.0/extensions/maven_plugin

## To check Web-Sockets functionality:

Just enter in `http://localhost:8099` and click `Connect`.  
Check heartbeat logs in browser developer console.
