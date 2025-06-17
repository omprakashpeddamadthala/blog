# Blog Application

This is a Spring Boot application for a blog.

## Technologies Used

- Java
- Spring Boot
- Maven
- PostgreSQL
- Docker

## How to Build and Run

### Prerequisites

- Java JDK 21 or later
- Maven 3.6.0 or later
- Docker (optional, for running with Docker)

### Build

```bash
./mvnw clean install
```

### Run

#### Without Docker

```bash
java -jar target/blog-0.0.1-SNAPSHOT.jar
```
The application will be available at http://localhost:9090.

#### With Docker

Build the Docker image:
```bash
docker build -t blog-app .
```

Run using Docker Compose:
```bash
docker-compose up
```
The application will be available at http://localhost:9090.

## API Endpoints

All endpoints are prefixed with `/api/v1`.

### Authentication (`/auth`)

- **`POST /login`**: Authenticates a user.
  - Request Body: `LoginRequest` (email, password)
  - Response: `AuthResponse` (token, expiresIn)

### Categories (`/categories`)

- **`GET /`**: Get all categories.
- **`POST /`**: Create a new category.
  - Request Body: `CategoryRequest` (name, description)
- **`DELETE /{id}`**: Delete a category by ID.

### Posts (`/posts`)

- **`GET /`**: Get all posts.
  - Optional Query Parameters: `categoryId`, `tagId`
- **`GET /drafts`**: Get all draft posts for the authenticated user.
- **`POST /`**: Create a new post.
  - Request Body: `PostRequestDTO` (title, content, categoryId, tagIds, status)
- **`PUT /{id}`**: Update a post by ID.
  - Request Body: `PostRequestDTO`
- **`GET /{id}`**: Get a post by ID.
- **`DELETE /{id}`**: Delete a post by ID.

### Tags (`/tags`)

- **`POST /`**: Create new tag(s).
  - Request Body: `TagRequest` (list of tag names)
- **`GET /`**: Get all tags.
- **`DELETE /{id}`**: Delete a tag by ID.

## Configuration

The main configuration file is `src/main/resources/application.properties`.
Key configurations include:
- Database connection details (`spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`)
- JWT secret key (`jwt.secret`)

## Contributing

Contributions are welcome! Please feel free to open an issue or submit a pull request.
