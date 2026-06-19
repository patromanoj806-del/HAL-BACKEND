package com.hal.guesthouse.repository;

import com.hal.guesthouse.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    List<Room> findByStatus(Room.RoomStatus status);
    List<Room> findByRoomTypeAndStatus(Room.RoomType type, Room.RoomStatus status);
    long countByStatus(Room.RoomStatus status);
}
