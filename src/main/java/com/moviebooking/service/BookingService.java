package com.moviebooking.service;

import com.moviebooking.model.Booking;
import com.moviebooking.model.Seat;
import com.moviebooking.model.Show;
import com.moviebooking.model.User;
import com.moviebooking.repository.BookingRepository;
import com.moviebooking.repository.SeatRepository;
import com.moviebooking.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final ShowRepository showRepository;
    private final EmailService emailService;

    @Transactional
    public Booking createBooking(User user, Long showId, List<String> seatNumbers) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));

        // Check if seats are available
        List<Seat> seats = seatRepository.findByShowId(showId);
        for (String seatNum : seatNumbers) {
            Seat seat = seats.stream()
                    .filter(s -> s.getSeatNumber().equals(seatNum))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Seat not found: " + seatNum));
            if (seat.isBooked()) {
                throw new RuntimeException("Seat already booked: " + seatNum);
            }
        }

        // Mark seats as booked
        for (String seatNum : seatNumbers) {
            seats.stream()
                    .filter(s -> s.getSeatNumber().equals(seatNum))
                    .forEach(s -> {
                        s.setBooked(true);
                        seatRepository.save(s);
                    });
        }

        // Update available seats count
        show.setAvailableSeats(show.getAvailableSeats() - seatNumbers.size());
        showRepository.save(show);

        // Create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setSeatsBooked(String.join(",", seatNumbers));
        booking.setNumberOfSeats(seatNumbers.size());
        booking.setTotalAmount(seatNumbers.size() * show.getTicketPrice());
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus("CONFIRMED");
        booking.setBookingReference(generateReference());

        Booking saved = bookingRepository.save(booking);
        log.info("Booking created: {}", saved.getBookingReference());

        // Send confirmation email
        emailService.sendBookingConfirmation(saved);

        return saved;
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if ("CANCELLED".equalsIgnoreCase(booking.getStatus())) {
            throw new RuntimeException("Booking is already cancelled.");
        }

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        // Free up seats
        List<Seat> showSeats = seatRepository.findByShowId(booking.getShow().getId());
        String[] cancelledSeats = booking.getSeatsBooked().split(",");
        for (String seatNum : cancelledSeats) {
            showSeats.stream()
                    .filter(s -> s.getSeatNumber().equals(seatNum.trim()))
                    .forEach(s -> {
                        s.setBooked(false);
                        seatRepository.save(s);
                    });
        }

        // Update show seat count
        Show show = booking.getShow();
        show.setAvailableSeats(show.getAvailableSeats() + cancelledSeats.length);
        showRepository.save(show);

        emailService.sendCancellationEmail(booking);
        log.info("Booking cancelled: {}", booking.getBookingReference());
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    private String generateReference() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "CB-" + timestamp;
    }
}
