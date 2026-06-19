package com.hal.guesthouse.controller;

import com.hal.guesthouse.model.Room;
import com.hal.guesthouse.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RoomController {

    private final RoomService roomService;

    // GET /api/rooms  -- All rooms (for room-availability.html)
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    // GET /api/rooms/available  -- Only available rooms
    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms() {
        return ResponseEntity.ok(roomService.getAvailableRooms());
    }

    // GET /api/rooms/available/{type}  -- Available by type (for book-room.html dropdown)
    @GetMapping("/available/{type}")
    public ResponseEntity<List<Room>> getAvailableByType(@PathVariable String type) {
        return ResponseEntity.ok(roomService.getAvailableRoomsByType(
                Room.RoomType.valueOf(type.toUpperCase())
        ));
    }

    // GET /api/rooms/{roomNumber}  -- Single room
    @GetMapping("/{roomNumber}")
    public ResponseEntity<Room> getRoom(@PathVariable String roomNumber) {
        return ResponseEntity.ok(roomService.getRoomByNumber(roomNumber));
    }

    // GET /api/rooms/stats  -- Room stats for dashboard
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getRoomStats() {
        return ResponseEntity.ok(roomService.getRoomStats());
    }

    // PUT /api/rooms/{roomNumber}/status  -- Update room status (admin)
    @PutMapping("/{roomNumber}/status")
    public ResponseEntity<?> updateStatus(@PathVariable String roomNumber,
                                           @RequestBody Map<String, String> body) {
        String status = body.get("status");
        Room room = roomService.updateRoomStatus(roomNumber, Room.RoomStatus.valueOf(status.toUpperCase()));
        return ResponseEntity.ok(room);
    }
}
