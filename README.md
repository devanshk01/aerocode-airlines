# ✈️ AeroCode Airlines – Full-Stack Airline Management System

> A production-ready airline management web application built with **Java 17+**, **Spring Boot 3.4**, **Thymeleaf**, **Spring Security**, and **H2/MySQL**.

---

## 📌 Project Overview

**AeroCode Airlines** is a full-stack web application that simulates a complete airline booking and management system. Originally developed as a console-based Java application during my OOP course at DA-IICT, it has been upgraded to a deployable, production-ready Spring Boot web application featuring:

- 🗄 **Relational database** (H2 for development, MySQL for production)
- 🔐 **Role-based security** (Admin vs. Passenger roles with BCrypt encryption)
- 🎨 **Premium modern UI** with responsive design and animations
- 📄 **Ticket export** to downloadable `.txt` files
- 📊 **Admin dashboard** with real-time analytics

---

## 🎯 Features

### Public Access
- 🔍 Search & filter flights by origin, destination, date, and type
- 🛩 Browse the entire aircraft fleet with specifications
- 📝 Register a new account

### Authenticated Users (Passengers)
- 🎫 Book flights with real-time pricing
- 📋 View and manage all bookings
- 🎟 View boarding-pass style tickets
- 📄 Download tickets as `.txt` files
- ❌ Cancel bookings (40% cancellation fee policy)

### Admin Only
- 📊 Analytics dashboard (flights, bookings, revenue, passengers)
- 👥 View all passenger data including sensitive info (phone, email, passport)
- ✈ Add/delete flights
- 📋 View all system bookings

---

## 🚀 Quick Start

### Prerequisites
| Tool | Version |
|------|---------|
| Java JDK | 17+ |
| Apache Maven | 3.8+ |

### Run the Application
```bash
# Clone the repo
git clone https://github.com/YOUR_USERNAME/AeroCode_airlines.git
cd AeroCode_airlines

# Set JAVA_HOME if needed (Windows PowerShell)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-XX"

# Build and run
mvn spring-boot:run
```

Open **http://localhost:8080** in your browser.

### Default Credentials
| Role | Username | Password |
|------|----------|----------|
| Admin | `admin` | `admin123` |
| Passenger | `devansh.kukadia` | `password123` |

> See [SETUP_AND_RUN.md](SETUP_AND_RUN.md) for detailed setup instructions.

---

## 🏗 Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17+ |
| Framework | Spring Boot 3.4 |
| Frontend | Thymeleaf + Custom CSS + JavaScript |
| Security | Spring Security 6 (BCrypt, RBAC) |
| Database | H2 (dev) / MySQL 8 (prod) |
| ORM | Spring Data JPA / Hibernate |
| Build | Apache Maven |

---

## 🗂️ Project Structure

```
AeroCode_airlines/
├── src/main/java/com/aerocode/airlines/
│   ├── AerocodeAirlinesApplication.java     # Spring Boot entry point
│   ├── config/                               # Security, Auth, DataLoader
│   ├── model/                                # JPA Entities
│   ├── repository/                           # Spring Data JPA interfaces
│   ├── service/                              # Business logic
│   └── controller/                           # MVC Controllers
├── src/main/resources/
│   ├── templates/                            # Thymeleaf HTML pages
│   ├── static/css/style.css                  # Premium design system
│   ├── static/js/main.js                     # UI interactions
│   ├── data/                                 # CSV seed data
│   └── application.properties               # Configuration
├── AeroCode_Airlines_main.java               # Original console version
├── pom.xml                                   # Maven dependencies
├── SETUP_AND_RUN.md                          # Detailed setup guide
└── README.md
```

---

## 🔐 Security Architecture

- **Authentication**: Form-based login with Spring Security
- **Password Storage**: BCrypt hashing (no plaintext passwords)
- **Authorization**: URL-based access control
  - Public: `/`, `/flights`, `/aircraft`, `/login`, `/register`
  - Authenticated: `/book/*`, `/my-bookings`, `/ticket/*`, `/export/*`
  - Admin Only: `/admin/**`
- **Data Privacy**: Sensitive passenger info (phone, email, passport) restricted to account owner & admin

---

## 💰 Business Logic (Preserved from Original)

| Feature | Formula |
|---------|---------|
| **Ticket Pricing** | `(1120 × fuelConsumption × distance) / (passengerCapacity + crewCapacity)` |
| **Cancellation Fee** | 40% of the original ticket price |
| **Refund** | 60% of the original ticket price |

---

## 📁 Data Migration

On first startup, the `DataLoader` automatically seeds the database from the original CSV files:
- **6 aircraft** (Boeing 747, 777, 737, Airbus A380, A340, A320)
- **100+ flights** from `airline_data.csv`
- **8 passengers** from `passenger_data.csv` (with auto-created user accounts)
- **1 admin account** (`admin` / `admin123`)

---

## 👤 Developed By

**Devansh Kukadia**  
Bachelor of Technology – Mathematics and Computing  
DA-IICT, 2025

---

## 📜 License

This project is for academic and portfolio use. Feel free to fork and modify with credits.
