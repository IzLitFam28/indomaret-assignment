# Klik Indomaret Backend Developer Technical Assignment

This repository contains the backend service for the Klik Indomaret Technical Assignment. It is a robust, configuration-driven Spring Boot REST API designed to manage master data (Provinces, Branches, Stores, and a Global Whitelist). The system is engineered to handle 20,000+ records with a focus on high performance, strict traceability, and non-destructive data integrity.

## Tech Stack

* **Java:** 17
* **Framework:** Spring Boot 4.1.2
* **Database:** PostgreSQL 18.6
* **ORM:** Spring Data JPA / Hibernate
* **Security:** Spring Security 6 + JJWT (Stateless JWT)
* **Migrations:** Flyway

## Prerequisites

* Java Development Kit (JDK) 17
* Maven 3.8+
* PostgreSQL 18+

## Quick Start Setup

### 1. Database Configuration

1. Open pgAdmin (or your PostgreSQL CLI) and create an empty database named `indomaret`.
2. Create the tables for the database with the following [link](https://drive.google.com/file/d/1tYe3clrihzJkQ3gql3Ypkd4amppud94R/view?usp=sharing)
2. The application uses Flyway for schema migration and a custom `DataSeeder` to automatically provision a test admin account and sample master data on startup.
3. If your local PostgreSQL credentials differ, update `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/indomaret (indomaret is the name of the database)
    username: postgres 
    password: your_password_here
```

### 2. Running the Application

Open your terminal in the root directory of this project and run:

```bash
mvn spring-boot:run
```

The embedded Tomcat server will start on `http://localhost:8080`. You will see `====== MASTER DATA SEEDED SUCCESSFULLY ======` in the console when the database is ready.

## Testing the API

A Postman collection can be created using the following cURL commands, or you can run them directly in your terminal.

### Step 1: Authentication (Get the JWT)

All core APIs are secured. You must first log in using the auto-provisioned admin account to receive a Bearer token.

**Request:**

```bash
curl -X POST "http://localhost:8080/api/v1/auth/login" \
-H "Content-Type: application/json" \
-d "{\"username\": \"admin\", \"password\": \"password\"}"
```

*Copy the `token` string from the response to use in the subsequent requests.*

### Step 2: Search Stores (Configuration-Driven)

This endpoint searches stores by province name. By default, it automatically includes whitelisted stores from *any* province.

**Request:**

```bash
curl -X GET "http://localhost:8080/api/v1/stores?provinceName=Jawa%20Barat&page=0&size=20" \
-H "Authorization: Bearer <YOUR_TOKEN_HERE>"
```

*(Note: To test the configuration toggle, append `&includeWhitelist=false` to the URL).*

### Step 3: Manage Global Whitelist

Add a store to the whitelist so it appears in all province searches regardless of its physical location.

**Request:**

```bash
curl -X POST "http://localhost:8080/api/v1/whitelist-stores" \
-H "Authorization: Bearer <YOUR_TOKEN_HERE>" \
-H "Content-Type: application/json" \
-d "{\"storeId\": 2, \"reason\": \"24-Hour National Flagship Store\"}"
```

### Step 4: Update & Soft-Delete Branch Data

Update a branch's details or soft-delete it. Based on `application.yaml` configurations, deletion may cascade to soft-delete all child stores.

**Update Request:**

```bash
curl -X PUT "http://localhost:8080/api/v1/branches/1" \
-H "Authorization: Bearer <YOUR_TOKEN_HERE>" \
-H "Content-Type: application/json" \
-d "{\"name\": \"Cabang Jakarta Pusat Updated\", \"isActive\": true}"
```

**Delete Request:**

```bash
curl -X DELETE "http://localhost:8080/api/v1/branches/1" \
-H "Authorization: Bearer <YOUR_TOKEN_HERE>"
```

## Traceability & Auditing

Every state-changing action performed through the API (Create, Update, Soft-Delete) is automatically tracked via JPA Auditing (`created_by`, `updated_by`) and recorded as a structured JSON state log in the `audit_logs` database table.

## Accompanying Documentation

Please refer to the included PDF deliverables for deeper architectural insights:

1. [Indomaret Assignment_API Documentation.pdf](https://drive.google.com/file/d/13xNx4loz5FDRyCOp9cS856uUB3ZDhwOj/view?usp=sharing)
2. [Jeremy_Backend Developer_DatabaseDesignDocumentation.pdf](https://drive.google.com/file/d/1LvJYubuT8uu9SdCG8zS0wfR_3AOt3itC/view?usp=sharing)