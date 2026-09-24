package com.moviebooking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "shows")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private String hallName;       // e.g. "Hall A", "Hall B"

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate showDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime showTime;

    private int totalSeats;
    private int availableSeats;
    private double ticketPrice;
}
