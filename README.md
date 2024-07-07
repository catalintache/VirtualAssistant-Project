# Virtual Assistant Project

## Overview
The Virtual Assistant Project is a  software solution designed to facilitate vehicle maintenance scheduling and offer management. It leverages advanced VIN decoding using the vindecoder.eu API and integrates with various services to provide users with accurate and tailored maintenance offers. Additionally, it uses ChatGPT to enhance user interactions.

## Features
- **Appointment Scheduling**: Users can book maintenance slots for their vehicles, selecting from available times and dates.
- **VIN Decoding**: Decodes Vehicle Identification Numbers (VIN) using the vindecoder.eu API to retrieve detailed vehicle information, ensuring accurate service offerings.
- **Offer Generation**: Generates maintenance offers based on vehicle specifications and user preferences, including oil type and filter brand.
- **User Interaction**: Provides an interactive interface for users to input their details and receive personalized responses from the virtual assistant, enhanced by ChatGPT.
- **Technical Support**: Assists users with technical queries related to vehicle maintenance.

## Technologies Used
- **Spring Boot**: Utilized for building the backend services.
- **Hibernate**: Employed for ORM and database interactions.
- **MySQL**: The relational database used for storing application data.
- **REST API**: Facilitates communication between different components and services.
- **vindecoder.eu API**: Used for decoding VINs to obtain vehicle details.
- **ChatGPT API**: Used for enhancing user interaction and providing intelligent responses.
- **Java**: The primary programming language used for development.

## Configuration
Sensitive information such as API keys and database credentials is stored in the `application.properties` file, which is not included in version control. Instead, you will find placeholder values for the API keys and database configuration:

```properties
# API Keys
vindecoder.api.key=INVALID_API_KEY
vindecoder.api.url=https://api.vindecoder.eu/3.2/d52a05f4fc2c/3da8c28385

chatgpt.api.key=INVALID_CHATGPT_API_KEY
chatgpt.api.url=https://api.openai.com

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/assisto
spring.datasource.username=root
spring.datasource.password=root123
