# Anti Fraud System

This is a Java Gradle Spring Boot project called "Anti Fraud System." The project is designed for educational purposes and is part of the Hyperskill curriculum.

## Description

The Anti Fraud System project aims to create a system for detecting and preventing fraudulent activities. It provides various API endpoints for user management, transaction handling, and managing suspicious IP addresses and stolen cards.

## Technologies Used

- Java
- Gradle
- Spring Boot
- Lombok
- Mapstruct
- Spring WEB

## Getting Started

To get started with the Anti Fraud System project, follow these steps:

1. Clone the repository: `git clone <repository-url>`
2. Make sure you have Java and Gradle installed on your system.
3. Open the project in your preferred IDE.
4. Build the project using Gradle.
5. Run the project.

## API Endpoints

The Anti Fraud System project provides the following API endpoints:

- ``PUT /api/auth/role``: Change user role. This endpoint requires a JSON request body containing the `username` and `role`. The response returns the updated user profile.
- ``PUT /api/auth/access``: Change user access. This endpoint requires a JSON request body containing the `username` and `operation`. The response returns the updated access status.
- ``PUT /api/antifraud/transaction``: Provide feedback for a transaction. This endpoint requires a JSON request body containing the `transactionId` and `feedback`. The response returns the updated transaction information.
- ``POST /api/antifraud/transaction``: Perform a new transaction. This endpoint requires a JSON request body containing the transaction details. The response returns the transaction response.
- ``POST /api/auth/user``: Register a new user. This endpoint requires a JSON request body containing the user details. The response returns the created user information.
- ``GET /api/antifraud/suspicious-ip``: Get the list of suspicious IP addresses.
- ``POST /api/antifraud/suspicious-ip``: Register a new suspicious IP address. This endpoint requires a JSON request body containing the IP address. The response returns the registered IP information.
- ``GET /api/antifraud/stolencard``: Get the list of stolen cards.
- ``POST /api/antifraud/stolencard``: Save a new stolen card. This endpoint requires a JSON request body containing the card number. The response returns the saved stolen card information.
- ``GET /api/auth/list``: Get the list of users.
- ``GET /api/antifraud/history``: Get the transaction history.
- ``GET /api/antifraud/history/{card-number}``: Get the transaction history for a specific card number.
- ``DELETE /api/auth/user/{username}``: Delete a user. This endpoint requires the username as a path parameter. The response returns the deleted user information.
- ``DELETE /api/antifraud/suspicious-ip/{ip}``: Delete a suspicious IP address. This endpoint requires the IP address as a path parameter. The response returns the deletion status.
- ``DELETE /api/antifraud/stolencard/{number}``: Delete a stolen card. This endpoint requires the card number as a path parameter. The response returns the deletion status.

## Acknowledgments

This project was developed as part of the educational curriculum on Hyperskill.
