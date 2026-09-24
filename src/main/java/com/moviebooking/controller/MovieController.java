package com.moviebooking.controller;

import com.moviebooking.model.Movie;
import com.moviebooking.service.MovieService;
import com.moviebooking.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final ShowRepository showRepository;

    // Home / Movie listing
    @GetMapping({"/", "/movies"})
    public String movieList(@RequestParam(required = false) String search,
                            @RequestParam(required = false) String genre,
                            Model model) {
        List<Movie> movies;
        if (search != null && !search.isBlank()) {
            movies = movieService.searchByTitle(search);
        } else if (genre != null && !genre.isBlank()) {
            movies = movieService.searchByGenre(genre);
        } else {
            movies = movieService.getNowShowingMovies();
        }
        List<Movie> featuredMovies = movies.stream().filter(Movie::isFeatured).toList();
        if (featuredMovies.isEmpty() && !movies.isEmpty()) {
            featuredMovies = movies.subList(0, Math.min(3, movies.size()));
        }
        model.addAttribute("movies", movies);
        model.addAttribute("featuredMovies", featuredMovies);
        model.addAttribute("search", search);
        model.addAttribute("genre", genre);
        return "movies/list";
    }

    // Movie detail + show timings
    @GetMapping("/movies/{id}")
    public String movieDetail(@PathVariable Long id, Model model) {
        Movie movie = movieService.getMovieById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        model.addAttribute("movie", movie);
        model.addAttribute("shows", showRepository.findByMovieId(id));
        model.addAttribute("today", java.time.LocalDate.now());
        model.addAttribute("tomorrow", java.time.LocalDate.now().plusDays(1));
        return "movies/detail";
    }
}
