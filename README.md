# E-Library Management System

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

The E-Library Management System is a web-based application developed using Spring framework, particularly focused on Spring REST API. It aims to provide a platform for managing digital resources, including books, educational materials, and user interactions.

## Features

- **Authentication:** Utilizes Domain-Driven Design for user authentication.
- **Spring Security with JWT:** Secure authentication using JSON Web Tokens.
- **Book Management:** Comprehensive book management system with CRUD operations.
- **Shopping Cart:** Allows users to add books to their cart for future checkout.
- **Notifications:** Sends notifications for important events.

## Technologies Used

- **Spring Framework:** Back-end development with Spring REST API.
- **Spring Security:** Secures the application with JWT-based authentication.
- **JWT (JSON Web Token):** Securely transmits information between parties as a JSON object.
- **MySQL:** Database management for storing books, users, and other data.
- **Logger4j:** Logging framework for recording system activity.
 
## Getting Started

### Prerequisites

- Java JDK 19
- Apache Maven
- MySQL Database 8.0

### Installation

1. Clone the repository: `git clone https://github.com/naymata/E-Library-Management-System`
2. Navigate to the project directory: `cd e-library`
3. Configure the database and security settings in `application.properties`.
4. Build the project: `mvn clean install`
5. Run the application: `java -jar target/e-library.jar`

### Usage

1. Access the application at `http://localhost:8080`.
2. Explore the features related to authentication, book management, shopping cart, and notifications.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
