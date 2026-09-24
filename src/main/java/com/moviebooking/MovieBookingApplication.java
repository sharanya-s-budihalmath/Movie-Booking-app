package com.moviebooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MovieBookingApplication {
    public static void main(String[] args) {
        SpringApplication.run(MovieBookingApplication.class, args);
        System.out.println("🎬 Cine is running at http://localhost:8080");
    }
}
