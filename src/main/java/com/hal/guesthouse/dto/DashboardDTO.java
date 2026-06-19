package com.hal.guesthouse.dto;

import lombok.Data;

public class DashboardDTO {

    @Data
    public static class DashboardStats {
        private long totalRooms;
        private long availableRooms;
        private long occupiedRooms;
        private long pendingRequests;
        private long totalBookings;
        private long approvedBookings;
        private long rejectedBookings;
    }
}
