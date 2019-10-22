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
- Retrieve all article in the system sorted by title

The Article JSON Payload returned by endpoint(s) must contain all properties of Article listed above.

 Business Logic layer of the Application must enforce the following rules:

- Non-editable properties cannot be updated/set via the create or update endpoints
- Article cannot be created or update with empty Title or Text or Author Id
- Article cannot be created with specified author id that does not exist in the **authors** table

Any business logic constraint violation or any runtime error should return a HTTP 500 error with short description.

The implementation must contain adequate unit tests to cover business requirements and edge cases such as missing arguments etc.

Application must build and produce an executable jar containing all dependencies.

The **authors** table should not be updatable via endpoints. Feel free to populate it with an arbitrary data via SQL.

# Build instructions

`mvn clean install`

# Usage description

##### Start application using Maven:
`mvn spring-boot:run`

##### Start application by running executable jar:
`java -jar target/articles-backend-app-0.0.1-SNAPSHOT.jar`

##### Build Docker image with application inside:
`docker build ./ -t articles-backend-app`

##### Start two Docker containers - with Postgres DB and with application:
`docker-compose up`

##### Swagger documentation situated here:
`http://localhost:8099/swagger-ui.html`

##### New article addition
`curl -i -H "Accept: application/json" -H "Content-Type: application/json" -d '{ "title": "Some tittle", "text": "Some text", "author": { "id": 1 } }' -X POST http://localhost:8099/articles`

##### Update existing article
`curl -i -H "Accept: application/json" -H "Content-Type: application/json" -d '{ "title": "Another tittle" }' -X PATCH http://localhost:8099/articles/2`

##### Get list of all articles
`curl -i -X GET http://localhost:8099/articles`

##### Deletion of article
`curl -i -X DELETE http://localhost:8099/articles/1`
