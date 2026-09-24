package com.moviebooking.controller;

import com.moviebooking.model.Movie;
import com.moviebooking.model.Show;
import com.moviebooking.service.BookingService;
import com.moviebooking.service.MovieService;
import com.moviebooking.service.UserService;
import com.moviebooking.model.Seat;
import com.moviebooking.repository.SeatRepository;
import com.moviebooking.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MovieService movieService;
    private final BookingService bookingService;
    private final UserService userService;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;

    // Admin dashboard
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalMovies", movieService.getAllMovies().size());
        model.addAttribute("totalBookings", bookingService.getAllBookings().size());
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("recentBookings", bookingService.getAllBookings());
        return "admin/dashboard";
    }

    // All movies
    @GetMapping("/movies")
    public String manageMovies(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("movie", new Movie());
        return "admin/movies";
    }

    // Add or update movie
    @PostMapping("/movies/save")
    public String saveMovie(@ModelAttribute Movie movie, RedirectAttributes ra) {
        movieService.saveMovie(movie);
        ra.addFlashAttribute("success", "Movie saved successfully!");
        return "redirect:/admin/movies";
    }

    // Delete movie
    @PostMapping("/movies/delete/{id}")
    public String deleteMovie(@PathVariable Long id, RedirectAttributes ra) {
        movieService.deleteMovie(id);
        ra.addFlashAttribute("success", "Movie deleted.");
        return "redirect:/admin/movies";
    }

    // Manage shows
    @GetMapping("/shows")
    public String manageShows(Model model) {
        model.addAttribute("shows", showRepository.findAll());
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("show", new Show());
        return "admin/shows";
    }

    @PostMapping("/shows/save")
    public String saveShow(@ModelAttribute Show show,
                           @RequestParam Long movieId,
                           RedirectAttributes ra) {
        Movie movie = movieService.getMovieById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        show.setMovie(movie);
        int total = show.getTotalSeats() > 0 ? show.getTotalSeats() : 30;
        show.setTotalSeats(total);
        show.setAvailableSeats(total);
        Show savedShow = showRepository.save(show);

        // Generate seat records for this show if non-existent
        if (seatRepository.findByShowId(savedShow.getId()).isEmpty()) {
            int seatsPerRow = 5;
            int totalRows = (int) Math.ceil((double) total / seatsPerRow);
            int count = 0;
            for (int r = 0; r < totalRows; r++) {
                char rowLetter = (char) ('A' + r);
                for (int s = 1; s <= seatsPerRow && count < total; s++) {
                    Seat seat = new Seat();
                    seat.setShow(savedShow);
                    seat.setSeatNumber(rowLetter + String.valueOf(s));
                    seat.setSeatType(r < 2 ? "VIP" : (r < 4 ? "PREMIUM" : "STANDARD"));
                    seat.setBooked(false);
                    seatRepository.save(seat);
                    count++;
                }
            }
        }

        ra.addFlashAttribute("success", "Show added successfully!");
        return "redirect:/admin/shows";
    }

    // All users
    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    // All bookings
    @GetMapping("/bookings")
    public String allBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "admin/bookings";
    }
}
