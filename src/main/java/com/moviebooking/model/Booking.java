package com.moviebooking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "show_id")
    private Show show;

    // Seats booked stored as comma-separated e.g. "A1,A2,B3"
    private String seatsBooked;

    private int numberOfSeats;
    private double totalAmount;
    private LocalDateTime bookingTime = LocalDateTime.now();

    // CONFIRMED, CANCELLED, PENDING
    private String status = "CONFIRMED";

    // Unique booking reference e.g. "CB-2026-00123"
    private String bookingReference;
}
