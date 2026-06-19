package com.hal.guesthouse.repository;

import com.hal.guesthouse.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingId(String bookingId);
    List<Booking> findByStatus(Booking.BookingStatus status);
    List<Booking> findByEmployee_PbNo(String pbNo);
    long countByStatus(Booking.BookingStatus status);
}
