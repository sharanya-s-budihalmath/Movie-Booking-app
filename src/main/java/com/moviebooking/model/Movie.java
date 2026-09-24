package com.moviebooking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String genre;
    private String language;
    private String duration;      // e.g. "2h 30m"
    private String description;
    private String posterUrl;     // image URL or path
    private double rating;
    private String releaseDate;
    private boolean nowShowing = true;
    private boolean featured = false;
    private String trailerUrl;
    @Column(name = "movie_cast")
    private String cast;
}
