# Dream Shops

This project is an e-commerce application built with Java 21, Spring Boot, and MySQL.

## Technologies Used

- **Java 21**: The latest version of Java for building robust and high-performance applications.
- **Spring Boot**: A framework for building production-ready applications quickly and easily.
- **MySQL**: A relational database management system for storing and managing application data.
- **Spring Security**: A powerful and customizable authentication and access control framework for securing Spring-based applications.

## Getting Started

### Prerequisites

- Java 21
- Maven
- MySQL

### Installation

1. **Clone the repository:**
    ```bash
    git clone https://github.com/rvega1204/spring-boot-dream-shops.git
    ```

2. **Set up the database:**
    ```sql
    CREATE DATABASE dream_shops;
    ```

3. **Configure the application:**
    Update the `application.properties` file with your MySQL database credentials.
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/dream_shops
    spring.datasource.username=yourusername
    spring.datasource.password=yourpassword
    ```

4. **Build and run the application:**
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

## Features

- User authentication and authorization
- Product catalog management
- Shopping cart and checkout
- Order management

## Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes. The project is free to use!

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
