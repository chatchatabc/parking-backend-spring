# ParkingABC Demo

This repository showcases a demo using `Kotlin Multiplatform Mobile` and `Kotlin Spring Boot`. This demo can also be
used as template for future projects needing `Android and iOS` implementation.

## Tech Stack

## Diagrams and Flowchart

### Entity Relationship Diagram

![Entity Relationship Diagram](./docs/erd.png)

### Flowchart

![Flowchart](./docs/flowchart.png)

## Goals

- Two Apps
    - User App
    - Parking Owner App
- One Admin Dashboard
- QR Code Scanning
- Invoicing
- Real Time Map
- Spring Boot Backend

## TODOs

- [x] Flowchart
- [x] UI Wireframe
- [x] Entity Relationship Diagram
- [ ] Redis
    - [ ] for Forgot Password
    - [x] for OTP
- [ ] User App
    - [ ] Android App
    - [ ] iOS App
- [ ] Parking Owner App
    - [ ] Android App
    - [ ] iOS App
- [ ] Admin Dashboard
    - [ ] Spring Boot Backend
    - [ ] React Frontend
- [ ] Spring Boot Backend
    - [x] User Registration
    - [x] Owner Registration
    - [x] Authentication
    - [ ] Logout (just delete token?)
    - [x] Vehicle API
    - [x] Parking Lot API
    - [x] Invoicing API
    - [ ] Scanning of QR Code
    - [ ] Real Time Map
- [ ] Add Surefire Plugin as dependency
    - sequence of plugins for pom
    - compiler first on build
    - maven surefire testing plugin
    - packaging
- [x] Add actuator dependency to all spring boots
    - [x] Admin
    - [x] API
- [ ] Unit Testing with Audrey
    - [ ] Database Track
    - [ ] Test Container
    - [ ] Spring Boot Test
    - [ ] MVC Test
    - [ ] DB Test
    - [ ] Unit Test on Domain
- [x] use flyway for init of data (dont use init or data init)

## Security TODO:

- [ ] NATS use same JWT as API
- [ ] NATS use same JWT as Admin
- [ ] Users with NATS should only be "listen" only
- [ ] Add more fields to user
    - [x] Integer ID (hidden, don't expose)
    - [x] Another UUID user id
    - [x] Another UUID for notification ID