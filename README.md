# SP1

BI-SP1.21 - Vybudování a uspořádání databáze dynamických textur (videí). 


Obecný popis webové aplikace:

- Web, kde můžete najít videa pomocí požadovaných charakteristik a tagů. Stahovat, nahravat a měnit nastavení je možné až po autorizaci.
 
- Jako další funkcianalitu na webu bude možné použít vybranou masku na vybranou dynamickou texturu a stáhnout nebo uložit výsledek. Také v budoucích iteracích bude možné rozšířit počet procesů které lze provádět s media objekty přidáním takových procesů, jako je odstranění šumu z obrázku, lepení videa a podobně.
 
- Základ klasifikaci (tagů) DynTex (http://dyntex.univ-lr.fr/classification_datasets/classification_datasets.html). 
- Základ anotačního systému DynTex (http://dyntex.univ-lr.fr/annotations/annotations.html). 
Technologie: 
- Beckend:  Kotlin, Spring, Postgres
- Frontend: React

## Table of Contents

1. [Project Goals](#project-goals)
2. [Website Features for Dynamic Textures](#website-features-for-dynamic-textures)
3. [Architecture Description](#architecture-description)
    - [Client Side (React.js)](#client-side-reactjs)
    - [Backend Services](#backend-services)
        - [Docker Container with Spring Boot](#docker-container-with-spring-boot)
        - [Python Microservice for Image Processing](#python-microservice-for-image-processing)
        - [Database Container (PostgreSQL)](#database-container-postgresql)
    - [Scalability and Maintainability](#scalability-and-maintainability)

## Project: Building and Organizing a Database of Dynamic Textures (BI-SP1.21)

### Project Goals
The "Building and Organizing a Database of Dynamic Textures" project aims to modernize and reorganize existing approaches to storing and manipulating dynamic textures. The main goals include:

1. **Data Collection and Creation** - Acquiring and creating a comprehensive collection of dynamic textures.
2. **Categorization and Typing** - Systematically organizing textures into categories by type.
3. **Interface Development** - Creating an intuitive user interface for easy access and data manipulation.
4. **Openness and Scalability** - Supporting community contributions and modular development for future expansion.

### Website Features for Dynamic Textures

The website for dynamic textures will offer a range of advanced features for users and developers, including:

- **Browsing and Searching** - Users will be able to browse the database of textures, filtering them by category, type, or used metrics.
- **Preview and Download** - The ability to play texture previews directly on the website and download textures for use in their own projects.
- **Applying Masks to Textures** - Allows users to apply various masks to textures to create unique effects.

This project delivers a modern solution for working with dynamic textures that will find application across a broad range of areas, from computer graphics to augmented reality.

## Architecture Description

![architecture](media/architecture.png)


### Client Side (React.js)

The client application is developed using React.js. It provides a user-friendly interface for interacting with the system. Users can upload images and perform various operations such as applying masks, filters, and other image processing tasks.

### Backend Services

#### Docker Container with Spring Boot

The backend services are implemented using Spring Boot, deployed within a Docker container. Spring Boot provides a robust framework for building and deploying Java-based applications. It serves as the intermediary between the client-side application and the image processing microservice. The API documentation can be found on [Swagger-generated page](http://localhost:8080/api/swagger-ui/index.html). 

#### Python Microservice for Image Processing

Within the Spring Boot container, a Python microservice is deployed to handle image processing tasks. This microservice utilizes Python libraries such as OpenCV or PIL to apply masks, filters, and other image manipulation operations based on user requests.

#### Database Container (PostgreSQL)

The system relies on a database container running PostgreSQL to store various data such as user information, processed image metadata, and configuration settings. Spring Boot interacts with the database container to perform CRUD operations and maintain data integrity.

### Communication Flow

1. **Client Interaction:** Users interact with the React.js client application, uploading images and specifying desired image processing tasks.
2. **Client to Spring Boot:** The client communicates with the Spring Boot backend through RESTful API endpoints. Requests are sent to the appropriate endpoints based on user actions.
3. **Spring Boot to Python Microservice:** Upon receiving image processing requests, Spring Boot forwards them to the Python microservice deployed within the same Docker container. The microservice processes the images based on the specified tasks.
4. **Python Microservice Execution:** The Python microservice performs image processing tasks using libraries like OpenCV or PIL. Once processing is complete, the resulting images are returned to Spring Boot.
5. **Spring Boot to Database:** Spring Boot interacts with the PostgreSQL database container to store processed image metadata, user information, and other relevant data. Database operations are performed through JDBC or Spring Data JPA.
6. **Database Response:** Upon successful database operations, Spring Boot returns appropriate responses to the client, confirming the completion of image processing tasks or handling errors gracefully.

### Scalability and Maintainability

- **Containerization:** Using Docker containers for both backend services and the database ensures portability and ease of deployment across different environments.
- **Microservice Architecture:** Decomposing the system into smaller, independent microservices promotes scalability, maintainability, and flexibility in development and deployment.
- **RESTful APIs:** Adopting RESTful API architecture facilitates communication between client and server, enabling seamless integration with different client applications and future expansions.
- **Continuous Integration and Deployment (CI/CD):** Implementing CI/CD pipelines automates the testing, building, and deployment processes, ensuring rapid iteration and deployment of new features and updates.
