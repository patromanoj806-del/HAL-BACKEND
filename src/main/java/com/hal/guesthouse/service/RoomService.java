package com.hal.guesthouse.service;

import com.hal.guesthouse.model.Room;
import com.hal.guesthouse.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public List<Room> getAvailableRooms() {
        return roomRepository.findByStatus(Room.RoomStatus.AVAILABLE);
    }

    public List<Room> getAvailableRoomsByType(Room.RoomType type) {
        return roomRepository.findByRoomTypeAndStatus(type, Room.RoomStatus.AVAILABLE);
    }

    public Room getRoomByNumber(String roomNumber) {
        return roomRepository.findById(roomNumber)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomNumber));
    }

    public Map<String, Long> getRoomStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", roomRepository.count());
        stats.put("available", roomRepository.countByStatus(Room.RoomStatus.AVAILABLE));
        stats.put("occupied", roomRepository.countByStatus(Room.RoomStatus.OCCUPIED));
        stats.put("reserved", roomRepository.countByStatus(Room.RoomStatus.RESERVED));
        stats.put("maintenance", roomRepository.countByStatus(Room.RoomStatus.MAINTENANCE));
        return stats;
    }

    public Room updateRoomStatus(String roomNumber, Room.RoomStatus status) {
        Room room = getRoomByNumber(roomNumber);
        room.setStatus(status);
        return roomRepository.save(room);
    }
}
