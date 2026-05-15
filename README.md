# BookApplication - Backend

This is my first REST API built with Spring Boot for managing books and users, with JWT authentication and GitHub OAuth2.

## Tech Stack

- Java 17
- Spring Boot 4.0.5
- Spring Security 7
- PostgreSQL
- JWT
- OAuth2 (GitHub)
- MapStruct
- Lombok
- Springdoc (Swagger UI)

---

## Requirements

- Java 17+
- Maven
- PostgreSQL
- A GitHub OAuth App (see below)

---

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/Kalixto73a/BookApplication-Backend-
cd BookApplication-Backend-
```

### 2. Create the PostgreSQL database

```sql
CREATE DATABASE booksdb;
```

### 3. Configure environment variables

Create a `.env` file in the root of the project based on `.env.example`:

```
JWT_SECRET_KEY=your_base64_jwt_secret_key
GITHUB_CLIENT_ID=your_github_client_id
GITHUB_CLIENT_SECRET=your_github_client_secret
```

#### Generating a JWT Secret Key

You can generate a secure Base64 key with the following command:

```bash
openssl rand -base64 32
```

#### Setting up GitHub OAuth2

1. Go to [GitHub Developer Settings](https://github.com/settings/developers)
2. Click **New OAuth App**
3. Fill in the following:
   - **Application name**: BookApplication (or any name)
   - **Homepage URL**: `http://localhost:8080`
   - **Authorization callback URL**: `http://localhost:8080/login/oauth2/code/github`
4. Copy the **Client ID** and generate a **Client Secret**
5. Paste them in your `.env` file

### 4. Run the application

```bash
./mvnw spring-boot:run
```

The API will start at `http://localhost:8080`.

On first run, the application will automatically create:
- An admin user with email `admin@admin.com` and password `admin` (this user us for the POST, PUT and DELETE methods)
- A list of sample books

---

## API Documentation

Once the application is running, you can explore the API via Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
```

Or import the `BookApplication.postman_collection.json` file into Postman to get all endpoints preconfigured with scripts and variables.

To test protected endpoints:
1. Register or login to get a JWT token
2. Click the **Authorize** button in Swagger UI
3. Enter your token and click **Authorize**

(In postman you only need to register or login because I created and script that automatically update the variable token and refreshToken)

---

### Login with GitHub

http://localhost:8080/oauth2/authorization/github

This is going to redirect you to GitHub for authentication. On success, returns a JWT token and refresh token.

---

## Roles

| Role  | Permissions                        |
|-------|------------------------------------|
| USER  | Read books                         |
| ADMIN | Read, create, update, delete books |

---

## Default Admin Credentials

| Field    | Value           |
|----------|-----------------|
| Email    | admin@admin.com |
| Password | admin           |


## ✍️🙍 Authors

- **Álvaro Cervera:**  [![GitHub](https://img.shields.io/badge/GitHub-Perfil-black?style=flat-square&logo=github)](https://github.com/Kalixto73a)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Perfil-blue?style=flat-square&logo=linkedin)](https://www.linkedin.com/in/álvaro-cervera-vigara-745576337/)
[![Correo](https://img.shields.io/badge/Email-Contacto-red?style=flat-square&logo=gmail)](mailto:Kalixto75@gmail.com)