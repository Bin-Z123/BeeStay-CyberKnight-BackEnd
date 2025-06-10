package com.poly.beestaycyberknightbackend.service;

import org.springframework.stereotype.Service;

import com.poly.beestaycyberknightbackend.domain.RoomType;
import com.poly.beestaycyberknightbackend.repository.RoomTypeRepository;

@Service
public class RoomTypeService {
    
    private final RoomTypeRepository roomTypeRepository;

    public RoomTypeService(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    public RoomType handleCreateRoomType(RoomType roomType) {
        return  this.roomTypeRepository.save(roomType);
    }

    
}
