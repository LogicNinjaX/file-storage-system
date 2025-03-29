---
# File Storage System 🚀📂🔐

## Overview

The **File Storage System** is a Spring Boot REST API that allows users to securely store and retrieve files using Amazon S3. The system supports user authentication and authorization using **JWT-based Spring Security**. Users can sign up, log in, and manage their files through a secure API. 🔄✅🔑

---
## Features 🎯📌✨

- **User Authentication & Authorization**: Implemented using **Spring Security with JWT**.
- **Secure File Storage**: Files are stored in **Amazon S3**.
- **RESTful API**: Designed with a clean and structured API architecture.
- **Database Integration**: Uses **MySQL** for storing user details.
- **DTO Mapping**: Utilizes **ModelMapper** for efficient object mapping.

---
## Technologies Used 🛠️📡💾

- **Spring Boot**
- **Spring Security (JWT-based authentication)**
- **Amazon S3 SDK**
- **MySQL Database**
- **ModelMapper**
---
## Dependencies 📦🔗📜

The project includes the following dependencies in `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.6</version>
</dependency>

<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>

<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>

<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
    <version>2.20.110</version>
</dependency>

<dependency>
    <groupId>org.modelmapper</groupId>
    <artifactId>modelmapper</artifactId>
    <version>3.1.1</version>
</dependency>
```

## Setup Instructions ⚙️🛠️🚀

### Prerequisites 📝💻🔍

- **Java 22** (or compatible version)
- **MySQL Database**
- **AWS S3 Bucket** with proper credentials - (Access Key, Secret Key, Region, Bucket Name)

### Installation Steps 📥📌🔧

1. Clone the repository:
   ```sh
   git clone https://github.com/your-repo/file-storage-system.git
   cd file-storage-system
   ```

   ## Folder Structure
```bash
PROJECT_FOLDER
│  README.md
│  pom.xml
|      
└──[src]      
│  └──[main]      
│     └──[java]
|     |   └──[com.example.MyApplication]
|     |      |
|     |      |---- MyApplication.java # main class
|     |      └──[config] # configuration classes
|     |      └──[controller] # contains rest controllers
|     |      └──[dto] # contains classes for data transfer objects
|     |      └──[entity] # contains database entities annotated with Hibernate & JPA annotations
|     |      └──[enums] # contains enums classes
|     |      └──[exception] # contains application related custom exception
|     |      └──[handler] # contains exception handler
|     |      └──[repository] # contains repository classes
|     |      └──[security] # contains security related classes for spring security and jwt
|     |      └──[service] # contains service classes
|     |
|     |
      └──[resources]
              └──[application.properties] #contains springboot cofigurations 
```

---
## 🔗ER DIAGRAM
![er_diagram](/src/main/resources/er_diagram.png)
---

2. Configure MySQL database in `application.properties`:
   ```properties
   spring.datasource.url=${db_url}
   spring.datasource.username=${db_username}
   spring.datasource.password=${db_password}
   spring.datasource.driver-class-name=${db_driver_class}
   ```
3. Configure AWS credentials:
   ```properties
   aws.accessKey=${access_key}
   aws.secretKey=${secret_key}
   aws.region=${region}
   aws.s3.bucket-name=${bucket_name}
   ```
4. Build and run the application:
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```

## API Endpoints 🔄🔗💡

| HTTP Method | Endpoint                       | Description             | Authentication |
| ----------- | ------------------------------ | ----------------------- | -------------- |
| POST        | `api/v1/register`              | User registration       | ❌ No           |
| POST        | `api/v1/login`                 | User login (JWT issued) | ❌ No           |
| POST        | `api/v1/s3/upload`             | Upload file to S3       | ✅ Yes          |
| DELETE      | `api/v1/s3/delete/{file-name}` | Delete file from S3     | ✅ Yes          |
| GET         | `api/v1/s3/files`              | Retrieve file metadata  | ✅ Yes          |

## Security & Authentication 🔐🛡️🔑

- Uses **JWT (JSON Web Tokens)** for authentication.
- Secure endpoints require **Authorization: Bearer <token>** header.

## Contributing 🤝🌍💡

Contributions are welcome! Please fork the repository and submit a pull request.

## License 📜⚖️✅

This project is licensed under the **MIT License**.

---

Feel free to modify and expand as needed! 🚀🎉💡
