package com.moviebooking.service;

import com.moviebooking.model.Movie;
import com.moviebooking.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {

    private static final Logger log = LoggerFactory.getLogger(MovieService.class);
    private final MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public List<Movie> getNowShowingMovies() {
        return movieRepository.findByNowShowing(true);
    }

    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public List<Movie> searchByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Movie> searchByGenre(String genre) {
        return movieRepository.findByGenreContainingIgnoreCase(genre);
    }

    public Movie saveMovie(Movie movie) {
        log.info("Saving movie: {}", movie.getTitle());
        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
        log.info("Deleted movie with id: {}", id);
    }
}
