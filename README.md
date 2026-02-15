# AllMart : E-Commerce API 🛒

A backend REST API built with Spring Boot for an e-commerce platform. This project provides a complete backend solution, handling everything from secure user authentication to product management, cart operations, and order processing.

## 🚀 Features

* **Authentication & Security:** Secure user registration and login using JWT (JSON Web Tokens). Role-based access control (Admin vs. Customer).
* **Product Management:** CRUD operations for products and categories.
* **Cloud Image Uploads:** Direct integration with Cloudinary for storing and serving product images (Admin-only access).
* **Shopping Cart:** Add, remove, and manage product quantities in a user-specific cart.
* **Order Processing:** Secure checkout system with stock validation, embedded shipping addresses for historical accuracy, and order history tracking.
* **Review System:** Customers can leave ratings and comments on products they have successfully purchased.
* **API Documentation:** Auto-generated, interactive API documentation using Swagger/OpenAPI.

## 🛠️ Tech Stack

* **Framework:** Java, Spring Boot
* **Database:** PostgreSQL, Spring Data JPA, Hibernate
* **Security:** Spring Security, JWT
* **Cloud Storage:** Cloudinary SDK
* **Documentation:** Springdoc OpenAPI (Swagger UI)
* **Tools:** Maven, Lombok

## ⚙️ Prerequisites

To run this project locally, you will need:
* Java 17 or higher
* PostgreSQL installed and running
* A Cloudinary account (for image uploads)

## 🏃‍♂️ Getting Started

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/sara-basta/AllMart.git
    cd AllMart
    ```

2.  **Configure environment variables:**
    Update your `src/main/resources/application.properties` file or set the following environment variables:
    ```properties
    # Database Configuration
    spring.datasource.url=jdbc:postgresql://localhost:5432/allmart
    spring.datasource.username=your_db_username
    spring.datasource.password=your_db_password
    spring.jpa.hibernate.ddl-auto=update

    # JWT Secret Key
    jwt.secret=your_super_secret_key_here

    # Cloudinary Configuration
    cloudinary.cloud_name=your_cloud_name
    cloudinary.api_key=your_api_key
    cloudinary.api_secret=your_api_secret
    ```

3.  **Run the application:**
   
Go to the main class `AllmartApplication.java` (in `src/main/java/com/sara/allmart`), and click the green **"Run"** button.

## 📖 API Reference

Once the server is running, you can access the Swagger UI to explore, test, and interact with the API endpoints:

* **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`

*(Note: To test protected endpoints, log in via the `/api/auth/login` endpoint, copy the JWT, and paste it into the "Authorize" modal in Swagger).*