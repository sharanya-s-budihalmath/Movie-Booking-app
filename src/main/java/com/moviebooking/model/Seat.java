package com.moviebooking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "seats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "show_id")
    private Show show;

    private String seatNumber;    // e.g. "A1", "B5"
    private String seatType;      // "STANDARD", "PREMIUM", "VIP"
    private boolean booked = false;
}
