package com.hal.guesthouse.service;

import com.hal.guesthouse.model.*;
import com.hal.guesthouse.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EmployeeRepository employeeRepository;
    private final RoomRepository roomRepository;

    // ---- CREATE BOOKING ----
    @Transactional
    public Booking createBooking(String pbNo, String guestName, String guestMobile,
                                  int numberOfGuests, java.time.LocalDate checkIn,
                                  java.time.LocalDate checkOut,
                                  Room.RoomType roomType,
                                  String purpose, String specialReq) {

        Employee employee = employeeRepository.findById(pbNo)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + pbNo));

        String bookingId = generateBookingId();

        Booking booking = Booking.builder()
                .bookingId(bookingId)
                .employee(employee)
                .guestName(guestName)
                .guestMobile(guestMobile)
                .numberOfGuests(numberOfGuests)
                .checkInDate(checkIn)
                .checkOutDate(checkOut)
                .preferredRoomType(roomType)
                .purposeOfVisit(purpose)
                .specialRequirements(specialReq)
                .status(Booking.BookingStatus.PENDING)
                .build();

        return bookingRepository.save(booking);
    }

    // ---- GET ALL BOOKINGS ----
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // ---- GET BOOKINGS BY STATUS ----
    public List<Booking> getBookingsByStatus(Booking.BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

    // ---- GET MY BOOKINGS ----
    public List<Booking> getMyBookings(String pbNo) {
        return bookingRepository.findByEmployee_PbNo(pbNo);
    }

    // ---- GET BOOKING BY ID ----
    public Booking getBookingById(String bookingId) {
        return bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));
    }

    // ---- ADMIN: APPROVE BOOKING + ASSIGN ROOM ----
    @Transactional
    public Booking approveBooking(String bookingId, String roomNumber) {
        Booking booking = getBookingById(bookingId);

        Room room = roomRepository.findById(roomNumber)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomNumber));

        if (room.getStatus() != Room.RoomStatus.AVAILABLE) {
            throw new RuntimeException("Room " + roomNumber + " is not available");
        }

        booking.setRoom(room);
        booking.setStatus(Booking.BookingStatus.APPROVED);

        // Mark room as reserved
        room.setStatus(Room.RoomStatus.RESERVED);
        roomRepository.save(room);

        return bookingRepository.save(booking);
    }

    // ---- ADMIN: REJECT BOOKING ----
    @Transactional
    public Booking rejectBooking(String bookingId) {
        Booking booking = getBookingById(bookingId);
        booking.setStatus(Booking.BookingStatus.REJECTED);
        return bookingRepository.save(booking);
    }

    // ---- CHECK IN ----
    @Transactional
    public Booking checkIn(String bookingId) {
        Booking booking = getBookingById(bookingId);

        if (booking.getStatus() != Booking.BookingStatus.APPROVED) {
            throw new RuntimeException("Booking must be APPROVED before check-in");
        }

        booking.setStatus(Booking.BookingStatus.CHECKED_IN);

        // Mark room as occupied
        Room room = booking.getRoom();
        if (room != null) {
            room.setStatus(Room.RoomStatus.OCCUPIED);
            roomRepository.save(room);
        }

        return bookingRepository.save(booking);
    }

    // ---- CHECK OUT ----
    @Transactional
    public Booking checkOut(String bookingId) {
        Booking booking = getBookingById(bookingId);

        if (booking.getStatus() != Booking.BookingStatus.CHECKED_IN) {
            throw new RuntimeException("Guest must be CHECKED IN before check-out");
        }

        booking.setStatus(Booking.BookingStatus.CHECKED_OUT);

        // Free the room
        Room room = booking.getRoom();
        if (room != null) {
            room.setStatus(Room.RoomStatus.AVAILABLE);
            roomRepository.save(room);
        }

        return bookingRepository.save(booking);
    }

    // ---- GENERATE BOOKING ID ----
    private String generateBookingId() {
        long count = bookingRepository.count() + 1;
        return String.format("BK%03d", count);
    }

    // ---- STATS ----
    public java.util.Map<String, Long> getStats() {
        java.util.Map<String, Long> stats = new java.util.HashMap<>();
        stats.put("total", bookingRepository.count());
        stats.put("pending", bookingRepository.countByStatus(Booking.BookingStatus.PENDING));
        stats.put("approved", bookingRepository.countByStatus(Booking.BookingStatus.APPROVED));
        stats.put("rejected", bookingRepository.countByStatus(Booking.BookingStatus.REJECTED));
        stats.put("checkedIn", bookingRepository.countByStatus(Booking.BookingStatus.CHECKED_IN));
        stats.put("checkedOut", bookingRepository.countByStatus(Booking.BookingStatus.CHECKED_OUT));
        return stats;
    }
}
