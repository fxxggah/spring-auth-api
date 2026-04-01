# 🔐 Auth API

API de autenticação desenvolvida com **Spring Boot** que implementa **JWT Authentication** com **Access Token** e **Refresh Token**, seguindo boas práticas de segurança e arquitetura backend.

O projeto também utiliza **Docker** para containerização da aplicação e do banco de dados **MySQL**, permitindo subir todo o ambiente com um único comando.

---

## 🚀 Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- MySQL
- Docker
- Docker Compose
- Maven

---

## 📌 Funcionalidades

- Registro de usuários
- Autenticação com login
- Geração de **Access Token**
- Geração de **Refresh Token**
- Renovação de Access Token com Refresh Token
- Proteção de endpoints com **Spring Security**
- Persistência de usuários no banco de dados
- API totalmente containerizada com **Docker**

---

## 🔑 Fluxo de Autenticação

```text
User Login
   ↓
Access Token (curta duração)
Refresh Token (longa duração)
   ↓
Access Token expira
   ↓
POST /auth/refresh
   ↓
Novo Access Token
```

---

## 📂 Estrutura do Projeto

```text
auth-api
│
├── src/main/java
│   ├── admin
│   ├── auth
│   ├── config
│   ├── dto
│   ├── exception
│   ├── security
│   └── user
│
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

---

## 🐳 Rodando o Projeto com Docker

### 1️⃣ Clonar o repositório

```bash
git clone https://github.com/fxxggah/spring-auth-api
cd auth-api
```

### 2️⃣ Subir os containers

```bash
docker compose up --build
```

Isso iniciará automaticamente:
- API Spring Boot
- Banco de dados MySQL

### 3️⃣ Acessar a API

`http://localhost:8080`

---

## 🗄️ Banco de Dados

O banco utilizado é MySQL, configurado automaticamente via Docker Compose.
Configuração padrão:

- **Database:** auth_db
- **User:** root
- **Password:** root
- **Port:** 3306

---

## 🔐 Endpoints de Autenticação

### Registrar Usuário
**POST** `/auth/register`

**Body**
```json
{
  "name": "Gabriel",
  "email": "gabriel@email.com",
  "password": "123456"
}
```

### Login
**POST** `/auth/login`

**Body**
```json
{
  "email": "gabriel@email.com",
  "password": "123456"
}
```

**Response**
```json
{
  "accessToken": "...",
  "refreshToken": "..."
}
```

### Refresh Token
**POST** `/auth/refresh`

**Body**
```json
{
  "refreshToken": "..."
}
```

**Response**
```json
{
  "accessToken": "novo_access_token",
  "refreshToken": "refresh_token"
}
```

---

## 🔒 Endpoint Protegido

Para acessar endpoints protegidos envie o Access Token no header:

```http
Authorization: Bearer seu_token
```

---

## 🧠 Aprendizados do Projeto

Este projeto demonstra conceitos importantes de backend:
- Implementação de JWT Authentication
- Uso de Refresh Tokens
- Segurança com Spring Security
- Containerização de aplicações com Docker
- Orquestração de containers com Docker Compose
- Comunicação entre containers
- Persistência com MySQL

---

## 👨‍💻 Autor

Desenvolvido por **Gabriel Oliveira**
Projeto criado para estudo de Desenvolvimento Backend, APIs seguras e containerização de aplicações.
