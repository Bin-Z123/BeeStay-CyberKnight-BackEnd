package com.poly.beestaycyberknightbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.beestaycyberknightbackend.domain.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
    long countByRoomStatus(String roomStatus);
    long count();
}
