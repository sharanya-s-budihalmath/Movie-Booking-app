package com.moviebooking.service;

import com.moviebooking.model.Booking;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    public void sendBookingConfirmation(Booking booking) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(booking.getUser().getEmail());
            message.setSubject("🎬 Booking Confirmed! - " + booking.getBookingReference());
            message.setText(buildBookingEmailBody(booking));
            mailSender.send(message);
            log.info("Booking confirmation email sent to {}", booking.getUser().getEmail());
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", booking.getUser().getEmail(), e.getMessage());
        }
    }

    public void sendCancellationEmail(Booking booking) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(booking.getUser().getEmail());
            message.setSubject("Booking Cancelled - " + booking.getBookingReference());
            message.setText(buildCancellationEmailBody(booking));
            mailSender.send(message);
            log.info("Cancellation email sent to {}", booking.getUser().getEmail());
        } catch (Exception e) {
            log.error("Failed to send cancellation email: {}", e.getMessage());
        }
    }

    private String buildBookingEmailBody(Booking booking) {
        return """
                Hi %s,

                Your booking is CONFIRMED! 🎉

                ─────────────────────────────
                Booking Reference : %s
                Movie             : %s
                Date & Time       : %s at %s
                Hall              : %s
                Seats             : %s
                Total Amount      : ₹%.2f
                ─────────────────────────────

                Please arrive 15 minutes before the show.
                Enjoy the movie!

                — Cine Team
                """.formatted(
                booking.getUser().getName(),
                booking.getBookingReference(),
                booking.getShow().getMovie().getTitle(),
                booking.getShow().getShowDate(),
                booking.getShow().getShowTime(),
                booking.getShow().getHallName(),
                booking.getSeatsBooked(),
                booking.getTotalAmount()
        );
    }

    private String buildCancellationEmailBody(Booking booking) {
        return """
                Hi %s,

                Your booking has been CANCELLED.

                Booking Reference : %s
                Movie             : %s
                Seats             : %s
                Refund Amount     : ₹%.2f

                The refund will be processed within 5-7 business days.

                — Cine Team
                """.formatted(
                booking.getUser().getName(),
                booking.getBookingReference(),
                booking.getShow().getMovie().getTitle(),
                booking.getSeatsBooked(),
                booking.getTotalAmount()
        );
    }
}
