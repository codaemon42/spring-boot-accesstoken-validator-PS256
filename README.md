# Spring Boot Access Token Validator (PS256)

This Spring Boot application validates PS256 Bearer tokens found in the `Authorization` header of incoming requests. It ensures that requests to protected resources are authenticated.

## Table of Contents

- [Features](#features)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Endpoints](#endpoints)
- [Project Structure](#project-structure)
- [Dependencies](#dependencies)

## Features

- Validates PS256 Bearer tokens using JWKS URI.
- Protects API endpoints with Spring Security.
- Logs details of authentication processes and potential errors.

## Getting Started

### Prerequisites

- Java 17
- Maven

### Installation

1. Clone the repository:
   ```sh
       git clone <repository_url>
       cd tokenvalidator
   ```
2. Build the project using Maven:
    ```bash 
      mvn clean install
    ```

### Configuration
- Open src/main/resources/application.properties and configure the following properties [application.properties](src%2Fmain%2Fresources%2Fapplication.properties):
   ```bash
      spring.application.name=spring boot accesstoken validator PS256
      server.port=8082
      jwks-uri=<Your JWKS URI>
   ```
   
### Running the Application
 - To run the application, use the following Maven command:\
   ```bash
    mvn spring-boot:run
   ```
 - The application will start on port 8082 (or the port configured in application.properties).


### Endpoints
#### Protected Resource
 - GET /protected-resource
   - Description: Access a protected resource.
   - Response:
     ```bash
     {
        "message": "This is a protected resource"
     }
     ```

### Project Structure
```bash
src
├── main
│   ├── java
│   │   └── com
│   │       └── codaemon
│   │           └── tokenvalidator
│   │               ├── configurations
│   │               │   └── SecurityConfiguration.java
│   │               ├── controllers
│   │               │   └── ProtectedController.java
│   │               └── filters
│   │                   └── SecurityFilters.java
│   └── resources
│       └── application.properties
└── test
    └── java
        └── com
            └── codaemon
                └── tokenvalidator
                    └── TokenValidatorApplicationTests.java

```
   
### Dependencies
 - Spring Boot Starter Web: Provides the necessary components for building web applications. 
 - Spring Boot Starter Security: Adds security capabilities to the application. 
 - Nimbus JOSE + JWT: Library for JWT processing and handling.

