package com.hal.guesthouse.dto;

import com.hal.guesthouse.model.Booking;
import com.hal.guesthouse.model.Room;
import lombok.Data;
import java.time.LocalDate;

public class BookingDTO {

    @Data
    public static class BookingRequest {
        private String pbNo;
        private String guestName;
        private String guestMobile;
        private int numberOfGuests;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
        private Room.RoomType preferredRoomType;
        private String purposeOfVisit;
        private String specialRequirements;
    }

    @Data
    public static class BookingResponse {
        private String bookingId;
        private String guestName;
        private String roomNumber;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
        private Booking.BookingStatus status;
        private String employeePbNo;
        private String employeeName;
        private String preferredRoomType;
    }

    @Data
    public static class ApprovalRequest {
        private String bookingId;
        private String assignedRoomNumber;
        private String action;
    }
}
