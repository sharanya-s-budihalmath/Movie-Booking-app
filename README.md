# 🎬 CineBook - Movie Booking System (Java Full Stack)

A complete full-stack movie ticket booking web application built with **Java Spring Boot**, **Spring MVC**, **Spring Security**, **MySQL**, **JDBC/JPA**, and **Thymeleaf + HTML5/CSS3/JavaScript** frontend.

Perfect for your resume project — this matches Java Full Stack Developer job descriptions (Spring Boot, Spring MVC, Spring REST, MySQL CRUD, JDBC, Microservices, JUnit/Mockito, Git).

---

## 🧩 Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.2, Spring MVC, Spring REST |
| Security | Spring Security (BCrypt password hashing, role-based access) |
| Database | MySQL + Spring Data JPA (Hibernate) |
| Frontend | Thymeleaf, HTML5, CSS3, JavaScript |
| Email | Spring Mail (Gmail SMTP) |
| Build Tool | Maven |

---

## ✨ Features Implemented

- ✅ **User Registration & Login** (Spring Security, BCrypt password encryption)
- ✅ **Movie Listing & Search** (search by title, filter by genre)
- ✅ **Seat Selection & Booking** (interactive seat map, VIP/Premium/Standard pricing)
- ✅ **Admin Panel** (manage movies, shows, users, view all bookings, dashboard stats)
- ✅ **Email Notifications** (booking confirmation & cancellation emails via Gmail SMTP)
- ✅ **Payment Page** (UI simulation — card/UPI/net-banking selection)
- ✅ **My Bookings** (view booking history, cancel bookings, auto seat release)

---

## 📁 Project Structure

```
movie-booking/
├── pom.xml
├── src/main/java/com/moviebooking/
│   ├── MovieBookingApplication.java     # Main entry point
│   ├── config/
│   │   ├── SecurityConfig.java          # Spring Security setup
│   │   └── DataSeeder.java              # Seeds sample movies + admin user
│   ├── controller/
│   │   ├── AuthController.java          # Login/Register
│   │   ├── MovieController.java         # Movie listing/detail
│   │   ├── BookingController.java       # Seats/Payment/Confirm/My Bookings
│   │   └── AdminController.java         # Admin dashboard/CRUD
│   ├── model/
│   │   ├── User.java, Movie.java, Show.java, Seat.java, Booking.java
│   ├── repository/                      # Spring Data JPA repositories
│   └── service/
│       ├── UserService.java, MovieService.java
│       ├── BookingService.java          # Core booking business logic
│       └── EmailService.java            # Email notifications
└── src/main/resources/
    ├── application.properties           # DB & mail config
    ├── static/css/style.css             # All styling
    ├── static/js/main.js                # Seat selection & payment JS
    └── templates/                       # Thymeleaf HTML pages
        ├── movies/ (list, detail)
        ├── booking/ (seats, payment, success, my-bookings)
        ├── auth/ (login, register)
        └── admin/ (dashboard, movies, shows, users, bookings)
```

---

## ⚙️ Setup Instructions

### 1. Prerequisites
- Java 17+ installed
- Maven installed
- MySQL Server running

### 2. Create Database
MySQL will auto-create the database, but make sure MySQL is running locally on port 3306.

### 3. Configure `application.properties`
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD

# For email — use a Gmail App Password (not your regular password)
spring.mail.username=your_email@gmail.com
spring.mail.password=your_16_char_app_password
```
> To get a Gmail App Password: Google Account → Security → 2-Step Verification → App Passwords.

### 4. Run the Application
```bash
cd movie-booking
mvn spring-boot:run
```

### 5. Open in Browser
```
http://localhost:8080
```

### 6. Login Credentials
- **Admin:** `admin@cinebook.com` / `admin123` (auto-created on first run)
- **User:** Register your own account at `/register`

---

## 🗄️ Database Tables (auto-created by Hibernate)

| Table | Purpose |
|---|---|
| `users` | Registered users & admin |
| `movies` | Movie catalog |
| `shows` | Show timings per movie (hall, date, time, price) |
| `seats` | Individual seats per show (booked/available) |
| `bookings` | Booking records with seat numbers, amount, status |

---

