package com.moviebooking.repository;

import com.moviebooking.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByShowId(Long showId);
    List<Seat> findByShowIdAndBooked(Long showId, boolean booked);
}
