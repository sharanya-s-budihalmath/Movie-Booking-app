package com.moviebooking.repository;

import com.moviebooking.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByNowShowing(boolean nowShowing);
    List<Movie> findByTitleContainingIgnoreCase(String title);
    List<Movie> findByGenreContainingIgnoreCase(String genre);
    List<Movie> findByLanguageIgnoreCase(String language);
}
