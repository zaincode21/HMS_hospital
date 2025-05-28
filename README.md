# Hospital Management System

A comprehensive hospital management system with OTP authentication, report generation, and CRUD operations using Hibernate.

## Prerequisites

1. Java JDK 11 or higher
2. Maven 3.6 or higher
3. MySQL 8.0 or higher
4. SMTP server details for email OTP (Gmail recommended)

## Database Setup

1. Install MySQL if not already installed
2. Create a new database:
```sql
CREATE DATABASE hms_db;
```
3. The tables will be automatically created by Hibernate

## Configuration

1. Update email settings in `src/main/java/hospitalmanagementsystem/LoginWithOTPController.java`:
   - Replace `your.email@gmail.com` with your Gmail address
   - Replace `your-app-specific-password` with your Gmail app password

2. Update database settings in `src/main/resources/hibernate.cfg.xml` if needed:
   - Default username: root
   - Default password: root
   - Default URL: jdbc:mysql://localhost:3306/hms_db

## Building and Running

1. Open terminal in project root directory
2. Build the project:
```bash
mvn clean install
```
3. Run the application:
```bash
mvn javafx:run
```

## Features

- OTP Authentication via Email/SMS
- Report Generation (PDF, Excel, CSV)
- Patient Management
- Doctor Management
- Appointment Scheduling
- Financial Records
- Session Management

## Ports Used

- Client Application: Default JavaFX port
- Server Ports: 81, 6000 (ensure these ports are available)