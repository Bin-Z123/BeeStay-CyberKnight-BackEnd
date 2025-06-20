package com.poly.beestaycyberknightbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.beestaycyberknightbackend.domain.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    long countByRoomStatus(String roomStatus);
    long count();
    Room findByRoomNumber(String roomNumber);
}
