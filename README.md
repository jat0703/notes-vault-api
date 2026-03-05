# Notes Vault API

## System Overview
A REST API that allows users to create, view, and delete notes. Built as part of a Backend Software Engineer Coding Challenge.

## Tech Choices
* **Java 21 & Spring Boot 4:** Used for clear, modern API design. Java Records are used for concise DTOs.
* **Spring Data JPA:** Chosen for simplification of code as complicated queries are not necessary for this application
* **PostgreSQL:** Chosen for robust relational data persistence.
* **Docker & Docker Compose:** Containerization ensures the project can be built and run locally without external dependencies.

## How to Run the Project and Tests
**To run the API and Database:**
1. Ensure Docker Desktop is running.
2. In the root directory, run: `docker-compose up --build -d`
3. The API will be available at `http://localhost:8080`.

**The automated tests will run automatically when the docker image is built. If you wish to run them locally:**
Ensure you have Java 21 installed, and run the following command in the root directory:
`./mvnw test` (Mac/Linux) or `mvnw.cmd test` (Windows).

## Data Model
**Entity:** `Note`

| Field      | Type      | Description                                                       |
|------------|-----------|-------------------------------------------------------------------|
| id         | `UUID`      | Primary Key. Automatically generated                              |
| content    | `Text`      | The body of the note. Validated to ensure it is not blank.        |
| created_at | `Timestamp` | Set automatically upon record creation.                           |
| updated_at | `Timestamp` | Tracks modifications. Remains null until the first update occurs. |
## API Usage Examples

### 1. Create a new note
**POST** `/notes`
```json
{
  "content": "This is a test note"
}
```

### 2. List all notes
**GET** `/notes`

### 3. List Notes Containing Keyword
**GET** `/notes?search={keyword}`

### 4. Get single note by ID
**Get** `/notes/{id}`

### 5. Delete a note
**DELETE** `/notes/{id}`

### 6. Update a note
**PUT** `/notes/{id}`
```json
{
  "content": "This is an updated test note"
}
```

## Assumptions, Tradeoffs, and Future Improvements
* **Assumptions:**
  * Content size does not need to be limited
    * If not the case content field in db can be changed to varchar(whatever size we want)
* **Tradeoffs:** 
  * spring.jpa.hibernate.ddl-auto=update was used to automatically generate the database schema for the sake of simplicity and quick local setup. **I WOULD NOT USE THIS IN A PRODUCTION ENVIRONMENT.**
  * Everyone is allowed to see all notes
    * Since I did not implement authorization there's no concept of "my notes" and "your notes"
* **Future Improvements:** 
  * Add pagination for when data set gets too large to fetch all at once
  * Implement a caching solution for frequently accessed notes. 
  * Add authentication and spring security
  * Updates tracking could be improved if rollbacks to previous note versions is deemed necessary