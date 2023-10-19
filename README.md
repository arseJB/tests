# Test project for Telefonica 

This is the test project that Telefonica asked for me to join them

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing
purposes.

### Prerequisites

* Docker

### Installing

Clone the project from Git repository

```
git clone https://github.com/arsejb/tests
```

Build the Docker Image: Open a terminal in the directory containing the Dockerfile and execute the following command to build the container image:

```
docker build -t rmclient .
```

Run the Container: Once the image has been built, you can run a container from it with the following command:

```
docker run -p 8081:8081 rmclient

```

Test will be executed during the "clean install" command launched during the docker build phase.

- EXECUTION
In order to execute the client: 
Open Postman or Chrome and execute:
```
http://localhost:8081/search-character-appearance?name=morty
```

## Architecture

I have chosen to implement a Controller-Service architecture in the project, due to the size and simplicity of the project, adhering to SOLID principles and clean code.

- Controller Layer: This layer is responsible for receiving HTTP client requests, processing those requests, and directing them to the corresponding service for performing the business logic.
- Service Layer: In this layer, you find the application's business logic. 

I employed the Singleton pattern for constructor-based dependency injection.

For defining the API, I have employed Open API 3.0, following the API First approach, entailing the utilization of an
OpenAPI YAML file containing the API specification.

I used RestTemplate to invoke external Rest services.

Additionally, I used Lombok in order to simplify the code

## Built With

* [Spring Boot](https://spring.io/projects/spring-boot) - The framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [Java 11] - Development language
* [Docker](https://www.docker.com/) - Contenerization
