package com.hal.guesthouse.controller;

import com.hal.guesthouse.model.*;
import com.hal.guesthouse.service.BookingService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    // POST /api/bookings  -- Submit new booking (from book-room.html)
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest req) {
        Booking booking = bookingService.createBooking(
                req.getPbNo(),
                req.getGuestName(),
                req.getGuestMobile(),
                req.getNumberOfGuests(),
                req.getCheckInDate(),
                req.getCheckOutDate(),
                Room.RoomType.valueOf(req.getPreferredRoomType()),
                req.getPurposeOfVisit(),
                req.getSpecialRequirements()
        );
        return ResponseEntity.ok(Map.of(
                "result", "BOOKING_SUBMITTED",
                "bookingId", booking.getBookingId()
        ));
    }

    // GET /api/bookings  -- All bookings (admin)
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    // GET /api/bookings/status/{status}  -- Filter by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Booking>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(bookingService.getBookingsByStatus(
                Booking.BookingStatus.valueOf(status.toUpperCase())
        ));
    }

    // GET /api/bookings/my/{pbNo}  -- Employee's own bookings (for booking-status.html)
    @GetMapping("/my/{pbNo}")
    public ResponseEntity<List<Booking>> getMyBookings(@PathVariable String pbNo) {
        return ResponseEntity.ok(bookingService.getMyBookings(pbNo));
    }

    // GET /api/bookings/{bookingId}  -- Single booking
    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getBooking(@PathVariable String bookingId) {
        return ResponseEntity.ok(bookingService.getBookingById(bookingId));
    }

    // PUT /api/bookings/{bookingId}/approve  -- Admin approve + assign room
    @PutMapping("/{bookingId}/approve")
    public ResponseEntity<?> approveBooking(@PathVariable String bookingId,
                                             @RequestBody Map<String, String> body) {
        String roomNumber = body.get("roomNumber");
        Booking booking = bookingService.approveBooking(bookingId, roomNumber);
        return ResponseEntity.ok(Map.of("result", "APPROVED", "bookingId", booking.getBookingId()));
    }

    // PUT /api/bookings/{bookingId}/reject  -- Admin reject
    @PutMapping("/{bookingId}/reject")
    public ResponseEntity<?> rejectBooking(@PathVariable String bookingId) {
        Booking booking = bookingService.rejectBooking(bookingId);
        return ResponseEntity.ok(Map.of("result", "REJECTED", "bookingId", booking.getBookingId()));
    }

    // PUT /api/bookings/{bookingId}/checkin  -- Check-in guest
    @PutMapping("/{bookingId}/checkin")
    public ResponseEntity<?> checkIn(@PathVariable String bookingId) {
        Booking booking = bookingService.checkIn(bookingId);
        return ResponseEntity.ok(Map.of("result", "CHECKED_IN", "bookingId", booking.getBookingId()));
    }

    // PUT /api/bookings/{bookingId}/checkout  -- Check-out guest
    @PutMapping("/{bookingId}/checkout")
    public ResponseEntity<?> checkOut(@PathVariable String bookingId) {
        Booking booking = bookingService.checkOut(bookingId);
        return ResponseEntity.ok(Map.of("result", "CHECKED_OUT", "bookingId", booking.getBookingId()));
    }

    // GET /api/bookings/stats  -- Dashboard stats
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        return ResponseEntity.ok(bookingService.getStats());
    }

    // ---- Inner Request Class ----
    @Data
    static class BookingRequest {
        private String pbNo;
        private String guestName;
        private String guestMobile;
        private int numberOfGuests;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
        private String preferredRoomType;
        private String purposeOfVisit;
        private String specialRequirements;
    }
}
