package com.moviebooking.controller;

import com.moviebooking.model.*;
import com.moviebooking.repository.SeatRepository;
import com.moviebooking.repository.ShowRepository;
import com.moviebooking.service.BookingService;
import com.moviebooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;

    // Seat selection page
    @GetMapping("/seats/{showId}")
    public String selectSeats(@PathVariable Long showId, Model model) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));
        List<Seat> seats = seatRepository.findByShowId(showId);
        model.addAttribute("show", show);
        model.addAttribute("seats", seats);
        return "booking/seats";
    }

    // Payment page
    @PostMapping("/payment")
    public String paymentPage(@RequestParam Long showId,
                              @RequestParam String selectedSeats,
                              Model model) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));
        List<String> seats = Arrays.asList(selectedSeats.split(","));
        double total = seats.size() * show.getTicketPrice();
        model.addAttribute("show", show);
        model.addAttribute("selectedSeats", selectedSeats);
        model.addAttribute("seatList", seats);
        model.addAttribute("total", total);
        return "booking/payment";
    }

    // Confirm booking
    @PostMapping("/confirm")
    public String confirmBooking(@RequestParam Long showId,
                                 @RequestParam String selectedSeats,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes ra) {
        try {
            User user = userService.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            List<String> seats = Arrays.asList(selectedSeats.split(","));
            Booking booking = bookingService.createBooking(user, showId, seats);
            ra.addFlashAttribute("booking", booking);
            return "redirect:/booking/success";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/booking/seats/" + showId;
        }
    }

    // Booking success
    @GetMapping("/success")
    public String success() {
        return "booking/success";
    }

    // My bookings
    @GetMapping("/my-bookings")
    public String myBookings(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("bookings", bookingService.getUserBookings(user.getId()));
        return "booking/my-bookings";
    }

    // Cancel booking
    @PostMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable Long id, RedirectAttributes ra) {
        try {
            bookingService.cancelBooking(id);
            ra.addFlashAttribute("success", "Booking cancelled successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/booking/my-bookings";
    }
}
