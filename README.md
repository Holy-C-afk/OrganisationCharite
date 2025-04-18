# Donation Campaign Management Mini-Service

This project is a donation campaign management service built using Spring Boot, Java 17+, Spring Data JPA, and an H2 in-memory database. It provides basic CRUD operations for managing donors and their contributions to various campaigns.

## Features

- **CRUD Operations for Donors**: Create, Read, Update, and Delete donor records.
- **Campaign Tracking**: Track donations made by donors and their progress toward campaign goals.
- **Active Donors**: Filter and view active donors who have ongoing campaigns.
- **Search Donors**: Search for donors by email and find the most contributed donors.
- **API Endpoints**: RESTful APIs to interact with the system.
- **Swagger Integration**: Documentation of the API using Swagger (Bonus feature).
- **Validation**: Ensures proper validation of fields like email format and non-empty fields.

## Requirements

- Java 17 or higher
- Spring Boot 2.5.x or higher
- Maven (for building the project)
- IntelliJ IDEA (recommended IDE)
- H2 In-memory Database (used for testing and development)

## Getting Started

### Clone the repository
```bash
git clone https://github.com/your-username/donation-campaign-management.git
cd donation-campaign-management
