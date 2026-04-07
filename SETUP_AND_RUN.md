# AeroCode Airlines – Setup & Run Guide

> A production-ready, full-stack airline management web application built with **Java 17+**, **Spring Boot 3.4**, **Thymeleaf**, **Spring Security**, and **H2/MySQL** database.

---

## 📋 Prerequisites

Before running this project, ensure you have the following installed:

| Tool | Version | Check Command |
|------|---------|---------------|
| **Java JDK** | 17 or higher | `java --version` |
| **Apache Maven** | 3.8+ | `mvn --version` |
| **Git** | Any | `git --version` |

> **Note:** If `JAVA_HOME` is not set, set it to your JDK installation directory:
> ```bash
> # Windows (PowerShell)
> $env:JAVA_HOME = "C:\Program Files\Java\jdk-XX"
>
> # Linux/Mac
> export JAVA_HOME=/usr/lib/jvm/java-XX
> ```

---

## 🚀 Quick Start (3 Steps)

### 1. Clone the Repository
```bash
git clone https://github.com/YOUR_USERNAME/Aerocode-Airlines.git
cd Aerocode-Airlines
```

### 2. Build the Project
```bash
# Windows
mvn clean compile

# Or if using Maven Wrapper (if present)
./mvnw clean compile
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

Then open your browser and navigate to:
```
http://localhost:8080
```

That's it! The application will automatically:
- Create an H2 file-based database in the `data/` directory
- Seed 6 aircraft, 100+ flights, and 8 passengers from the original CSV data
- Create a default admin account

---

## 🔐 Default Credentials

| Role | Username | Password |
|------|----------|----------|
| **Admin** | `admin` | `admin123` |
| **Passenger** (Example) | `devansh.kukadia` | `password123` |
| **Passenger** (Example) | `sadana.dharavath` | `password123` |

> All seeded passenger accounts use the password `password123`.

---

## 🗺 Application Pages

| Page | URL | Access |
|------|-----|--------|
| 🏠 Landing Page | `/` | Public |
| 🔍 Search Flights | `/flights` | Public |
| 🛩 Aircraft Fleet | `/aircraft` | Public |
| 🔐 Login | `/login` | Public |
| 📝 Register | `/register` | Public |
| 🎫 Book a Flight | `/book/{flightId}` | Authenticated |
| 📋 My Bookings | `/my-bookings` | Authenticated |
| 🎟 View Ticket | `/ticket/{bookingId}` | Owner / Admin |
| 📄 Download Ticket | `/export/ticket/{bookingId}` | Owner / Admin |
| 📊 Admin Dashboard | `/admin/dashboard` | Admin Only |
| 👥 All Passengers | `/admin/passengers` | Admin Only |
| ✈ Manage Flights | `/admin/flights` | Admin Only |
| 📋 All Bookings | `/admin/bookings` | Admin Only |

---

## 🏗 Project Architecture

```
Aerocode Airlines/
├── src/
│   ├── main/
│   │   ├── java/com/aerocode/airlines/
│   │   │   ├── AerocodeAirlinesApplication.java   # Entry point
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java             # Spring Security
│   │   │   │   ├── CustomUserDetailsService.java   # Auth bridge
│   │   │   │   └── DataLoader.java                 # DB seeder
│   │   │   ├── model/                              # JPA Entities
│   │   │   │   ├── User.java, Role.java
│   │   │   │   ├── Flight.java, Aircraft.java
│   │   │   │   ├── Passenger.java, Booking.java
│   │   │   ├── repository/                         # Spring Data JPA
│   │   │   ├── service/                            # Business logic
│   │   │   │   ├── FlightService.java              # Pricing formula
│   │   │   │   ├── BookingService.java             # Booking + cancel
│   │   │   │   └── TicketExportService.java        # .txt export
│   │   │   └── controller/                         # MVC Controllers
│   │   │       ├── HomeController.java
│   │   │       ├── AuthController.java
│   │   │       ├── FlightController.java
│   │   │       ├── BookingController.java
│   │   │       ├── AdminController.java
│   │   │       └── TicketExportController.java
│   │   └── resources/
│   │       ├── application.properties              # H2 config
│   │       ├── application-mysql.properties        # MySQL config
│   │       ├── data/                               # CSV seed data
│   │       ├── static/css/style.css                # Design system
│   │       ├── static/js/main.js                   # UI interactions
│   │       └── templates/                          # Thymeleaf views
│   │           ├── index.html                      # Landing page
│   │           ├── login.html, register.html       # Auth
│   │           ├── flights.html, aircraft.html     # Public pages
│   │           ├── booking.html, my-bookings.html  # User pages
│   │           ├── ticket.html                     # Ticket view
│   │           ├── fragments/layout.html           # Shared navbar/footer
│   │           └── admin/                          # Admin pages
├── pom.xml                                         # Maven dependencies
└── AeroCode_airlines/                              # Original project
```

---

## ✨ Key Features

### 🔐 Security & Privacy
- **BCrypt** password hashing for all user accounts
- **Role-Based Access Control** (RBAC): Admin vs Passenger
- Sensitive data (phone, email, passport) restricted to **Admin** and **account owner**
- CSRF protection and secure session management

### 📊 Admin Dashboard
- Real-time analytics: total flights, bookings, revenue, passengers
- Full passenger data view (sensitive info – admin only)
- Flight management (add/delete flights)
- Complete booking overview

### ✈ Booking System
- Search flights by origin, destination, date, and type
- Fuel-based dynamic pricing formula (preserved from original project)
- International flight passport validation
- 40% cancellation fee policy (preserved from original project)

### 📄 Ticket Export
- Download formatted `.txt` ticket files
- Includes all flight & passenger details
- Available from My Bookings and Ticket View pages

---

## 🗄 Database Configuration

### Default: H2 (File-based, zero config)
The application uses H2 by default. Data persists across restarts in the `data/` directory.

- **H2 Console**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:file:./data/aerocode_db`
- **Username**: `sa` | **Password**: *(empty)*

### Production: MySQL
To switch to MySQL:

1. Create a MySQL database:
   ```sql
   CREATE DATABASE aerocode_airlines;
   ```

2. Run with the MySQL profile:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=mysql
   ```

3. Update credentials in `src/main/resources/application-mysql.properties` if needed.

---

## 🔧 Troubleshooting

| Issue | Solution |
|-------|----------|
| `JAVA_HOME not set` | Set `JAVA_HOME` environment variable to your JDK path |
| Port 8080 in use | Change port in `application.properties`: `server.port=9090` |
| Database locked | Delete the `data/` folder and restart (resets all data) |
| Maven not found | Ensure Maven is in your system PATH |

---

## 📝 Tech Stack Summary

| Layer | Technology |
|-------|-----------|
| Language | Java 17+ |
| Framework | Spring Boot 3.4.4 |
| Frontend | Thymeleaf + Custom CSS + JavaScript |
| Security | Spring Security 6 (BCrypt, RBAC) |
| Database | H2 (dev) / MySQL 8 (prod) |
| ORM | Spring Data JPA / Hibernate |
| Build Tool | Apache Maven |

---

## 👨‍💻 Developer

**Devansh Kukadia** – DA-IICT  
Originally developed as an OOP course project, upgraded to a full-stack production-ready web application.

---

*Built with ☕ Java and ✈ passion for aviation.*
