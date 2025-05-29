# E-Commerce Platform with Spring Boot

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.1.0-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.1-lightgrey)

A full-featured e-commerce platform built with Spring Boot, featuring product management, shopping cart, user authentication, and order processing.

## Features

- **User Management**
  - Registration and authentication
  - Role-based authorization (Admin/Customer)
  - User profile management

- **Product Catalog**
  - Product CRUD operations
  - Category management
  - Product search and filtering
  - Image upload for products

- **Shopping Cart**
  - Add/remove products
  - Update quantities
  - Persistent cart for logged-in users

- **Order Processing**
  - Checkout process
  - Order history
  - Order status tracking

- **Admin Dashboard**
  - Manage products and categories
  - View all orders
  - Manage user accounts

## Technologies Used

- **Backend**
  - Spring Boot 3.1.0
  - Spring Security
  - Spring Data JPA
  - Hibernate
  - MySQL

- **Frontend**
  - Thymeleaf templates
  - Bootstrap 5
  - jQuery
  - HTML5/CSS3

- **Tools**
  - Maven
  - Lombok
  - Hibernate Validator

## Prerequisites

Before running the application, ensure you have:

- Java 17 JDK installed
- MySQL 8.0+ server running
- Maven 3.8.4+ installed

## Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/ecommerce-project.git
   cd ecommerce-project