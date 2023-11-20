# Appointment - Sample Java Spring Boot Project
This is a sample Java / Maven / Spring Boot REST application that can be used for creating, updating, viewing appointments

## Project is built using Java v8.x Sprint boot v2.6.x

## Project follows Service Oriented Architecture(SOA)

## Application Port
server.port - 8080

## Configuration URLS
http://localhost:8080/

## Steps to run
1. Clone this repository
2. Make sure you are using JDK 1.8 and Maven 3.x
3. Build the project using mvn clean install
4. Run using mvn spring-boot:run
5. The web application is accessible with swagger via localhost:8080/swagger-ui.html

## About the service
The service is just a simple appointment application REST service. It uses an in-memory database (H2) to store the data. The REST end points are defined on the controller. It has various CRUD methods like create, update, delete, get and get all appointments.

## Postman API documentation
The API documentation is been added in the repository. It contains all the resource details with sample request example and response example. Please download it.

## To view Swagger 2 API docs
Run the server and browse to localhost:8080/swagger-ui.html

## To view the H2 in-memory datbase
The service runs on H2 in-memory database. To view and query the database you can browse to http://localhost:8090/h2-console.
Driver Class - 
JDBC Url - jdbc:h2:mem:dcbapp. 
Default username - sa 
password - password
