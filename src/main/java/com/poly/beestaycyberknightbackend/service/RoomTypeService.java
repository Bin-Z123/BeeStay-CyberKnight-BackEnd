package com.poly.beestaycyberknightbackend.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.poly.beestaycyberknightbackend.domain.RoomType;
import com.poly.beestaycyberknightbackend.repository.RoomTypeRepository;
import com.poly.beestaycyberknightbackend.util.error.ResourceNotFoundException;

@Service
public class RoomTypeService {
    
    private final RoomTypeRepository roomTypeRepository;

    public RoomTypeService(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    public RoomType handleCreateRoomType(RoomType roomType) {
        return  this.roomTypeRepository.save(roomType);
    }

    public void handleDeleteRoomType(long id) {
        this.roomTypeRepository.deleteById(id);
    }

    public RoomType fetchRoomTypeById(long id) {
        return roomTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loại phòng với ID " + id + " không tồn tại"));
    }

}
