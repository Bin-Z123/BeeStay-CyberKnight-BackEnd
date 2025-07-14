package com.poly.beestaycyberknightbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.beestaycyberknightbackend.domain.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    long countByRoomStatus(String roomStatus);

    long count();

    Room findByRoomNumber(String roomNumber);

    @Query(value = """
            SELECT r.* FROM RoomTypes rt RIGHT JOIN Rooms r ON rt.id = r.roomtype_id
            		WHERE rt.name LIKE CONCAT('%', :roomType, '%') AND r.roomstatus = 'INACTIVE';
            """, nativeQuery = true)
    List<Room> findInactiveRoomsByRoomType(String roomType);

    @Query(value = """
                        SELECT DISTINCT rt.name, r.* FROM Rooms r LEFT JOIN Stays s ON r.id = s.room_id
            									JOIN RoomTypes rt ON r.roomtype_id = rt.id
            									WHERE r.roomstatus NOT LIKE '%FIX%' AND rt.id = :roomTypeId
            									AND NOT EXISTS (
            									SELECT 1 FROM Stays s2 JOIN Bookings b ON s2.booking_id = b.id WHERE s2.room_id = r.id AND s2.staystatus = 'NOW'
            );
                        """, nativeQuery = true)
    List<Object[]> getRoomsAvailable(int roomTypeId);

}
