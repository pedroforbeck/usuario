<div align="center">

  <h1 style="color: #FFFFFF; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;">
    <b>IDENTITY & USER SERVICE</b>
  </h1>
  <p style="color: #A1A1A6;"><i>Core Microservice for Authentication and Access Management</i></p>

  <a href="https://github.com/pedroforbeck/usuario">
    <img src="https://readme-typing-svg.demolab.com?font=-apple-system,BlinkMacSystemFont,San+Francisco,Helvetica+Neue&weight=400&size=14&duration=4000&pause=1000&color=A1A1A6&center=true&vCenter=true&width=600&lines=Identity+%26+Access+Management+(IAM);Stateless+JWT+Issuance;Role-Based+Access+Control+(RBAC);Secure+Credential+Storage" alt="Typing SVG" />
  </a>

  <br><br>

  <img src="https://img.shields.io/badge/Java_17-1C1C1E?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/Spring_Boot-1C1C1E?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Spring_Security-1C1C1E?style=for-the-badge&logo=springsecurity&logoColor=white" alt="Spring Security" />
  <img src="https://img.shields.io/badge/PostgreSQL-1C1C1E?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL" />

  <br><br>

  <img src="https://img.shields.io/badge/Role-Identity%20Provider%20(IdP)-1C1C1E?style=for-the-badge&logo=auth0&logoColor=white" alt="Role" />
  <img src="https://img.shields.io/badge/Security-BCrypt%20Hashing-1C1C1E?style=for-the-badge&logo=letsencrypt&logoColor=white" alt="Security" />
  <img src="https://img.shields.io/badge/Network-Port%208081-1C1C1E?style=for-the-badge&logo=ngrok&logoColor=white" alt="Port" />

</div>

<br><br>

> **Abstract**<br>
> This repository contains the **User Service**, acting as the central Identity Provider (IdP) for the Task Scheduler ecosystem. It is strictly responsible for managing user lifecycle, securely hashing credentials, establishing Role-Based Access Control (RBAC), and issuing stateless JSON Web Tokens (JWT) for ecosystem-wide authentication.

<br>

## <img src="https://icongr.am/feather/layers.svg?size=24&color=A1A1A6" align="absmiddle" /> Table of Contents

- [Service Architecture](#-service-architecture)
- [Core Capabilities](#-core-capabilities)
- [Deployment & Setup](#-deployment--setup)
- [API Endpoints Overview](#-api-endpoints-overview)

---

## <img src="https://icongr.am/feather/cpu.svg?size=24&color=A1A1A6" align="absmiddle" /> Service Architecture

This microservice operates behind the ecosystem's API Gateway. It does not communicate directly with other domain services, ensuring a strict boundary around identity data.

<br>

<details>
<summary><b style="color: #A1A1A6; cursor: pointer;">View Component Topology (Glass/Wireframe Diagram)</b></summary>
<br>

```mermaid
graph LR;
    %% Glassmorphism / Apple Aesthetic Styling
    classDef default fill:none,stroke:#A1A1A6,stroke-width:1px,color:#A1A1A6,rx:8,ry:8;
    classDef highlight fill:none,stroke:#FFFFFF,stroke-width:2px,color:#FFFFFF,rx:12,ry:12;
    classDef db fill:none,stroke:#007AFF,stroke-width:1px,color:#007AFF,rx:4,ry:4;

    %% Nodes
    Gateway[BFF Gateway / Client]:::default
    UserService{User Service\nPort 8081}:::highlight
    DB[(PostgreSQL\nUsers Schema)]:::db

    %% Connections
    Gateway -- "Authentication Request\n(Username / Password)" --> UserService
    UserService -- "Issues JWT Bearer" --> Gateway
    UserService <--> "Persists Profile & Roles" DB
```
</details>

---

## <img src="https://icongr.am/feather/command.svg?size=24&color=A1A1A6" align="absmiddle" /> Core Capabilities

| Feature | Description |
| :--- | :--- |
| <img src="https://icongr.am/feather/key.svg?size=18&color=A1A1A6" align="absmiddle" /> **JWT Issuance** | Generates cryptographically signed tokens to maintain stateless ecosystem sessions. |
| <img src="https://icongr.am/feather/lock.svg?size=18&color=A1A1A6" align="absmiddle" /> **Credential Hashing** | Secures user passwords utilizing strong `BCrypt` hashing algorithms. |
| <img src="https://icongr.am/feather/users.svg?size=18&color=A1A1A6" align="absmiddle" /> **User Lifecycle** | Complete CRUD operations for identity profiles. |
| <img src="https://icongr.am/feather/shield.svg?size=18&color=A1A1A6" align="absmiddle" /> **Role Management** | Associates user identities with specific system permissions (Admin, User, etc.). |
| <img src="https://icongr.am/feather/database.svg?size=18&color=A1A1A6" align="absmiddle" /> **Data Isolation** | Maintains an independent PostgreSQL schema strictly for identity state. |

---

## <img src="https://icongr.am/feather/terminal.svg?size=24&color=A1A1A6" align="absmiddle" /> Deployment & Setup

To run this microservice in isolation, ensure you have **Java 17+**, **Maven 3.8+**, and **PostgreSQL** installed.

### 1. Database Configuration
Create a dedicated database/schema in your PostgreSQL instance for this service (e.g., `db_usuario`).

### 2. Environment Variables
Configure your `application.properties` or `application.yml` with your local credentials. The required variables are:

```yaml
# Server Configuration
server.port: 8081

# Database Configuration
spring.datasource.url: jdbc:postgresql://localhost:5432/db_usuario
spring.datasource.username: your_postgres_user
spring.datasource.password: your_postgres_password
spring.jpa.hibernate.ddl-auto: update

# Security (Must match the secret in the BFF Gateway)
api.security.token.secret: your_super_secret_key_here
```

### 3. Build & Execute
Navigate to the project root directory and start the Spring Boot application:

```bash
# Clone the repository
git clone [https://github.com/pedroforbeck/usuario.git](https://github.com/pedroforbeck/usuario.git)

# Navigate to the directory
cd usuario

# Run the application
./mvnw spring-boot:run
```

---

## <img src="https://icongr.am/feather/globe.svg?size=24&color=A1A1A6" align="absmiddle" /> API Endpoints Overview

Although this service runs on `http://localhost:8081`, requests should ideally be routed through the BFF Gateway. Below are the primary domain endpoints exposed by this service:

| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :---: |
| `POST` | `/auth/login` | Authenticates a user and returns a JWT. | No |
| `POST` | `/auth/register` | Registers a new identity in the database. | No |
| `GET` | `/users/{id}` | Retrieves specific user profile details. | Yes |
| `GET` | `/users` | Lists paginated users (Admin only). | Yes |

*(Note: Full interactive API documentation is accessible via Swagger UI when the service is running).*

---

<div align="center">
  <br>
  <p style="color: #A1A1A6;">Architected and maintained by <b><a href="https://github.com/pedroforbeck" style="color: #A1A1A6; text-decoration: none;">Pedro Forbeck</a></b>.</p>
  <p>
    <a href="https://github.com/pedroforbeck">
      <img src="https://img.shields.io/badge/GitHub-1C1C1E?style=flat-square&logo=github&logoColor=white" alt="GitHub" />
    </a>
    <a href="https://www.linkedin.com/in/pedro-forbeck-180a98390/">
      <img src="https://img.shields.io/badge/LinkedIn-1C1C1E?style=flat-square&logo=linkedin&logoColor=white" alt="LinkedIn" />
    </a>
  </p>
</div>
