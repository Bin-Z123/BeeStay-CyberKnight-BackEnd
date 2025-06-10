package com.poly.beestaycyberknightbackend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.poly.beestaycyberknightbackend.domain.RoomType;
import com.poly.beestaycyberknightbackend.domain.dto.request.RoomTypeCreation;
import com.poly.beestaycyberknightbackend.domain.dto.response.RoomTypeResponse;
import com.poly.beestaycyberknightbackend.mapper.RoomTypeMapper;
import com.poly.beestaycyberknightbackend.repository.RoomTypeRepository;
import com.poly.beestaycyberknightbackend.util.error.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomTypeService {
    
    private final RoomTypeRepository roomTypeRepository;
    private final RoomTypeMapper roomTypeMapper;

    public RoomTypeResponse handleCreateRoomType(RoomTypeCreation roomTypeCreation) {
        RoomType roomType = roomTypeMapper.toRoomType(roomTypeCreation);
        return  roomTypeMapper.toRoomTypeResponse(roomTypeRepository.save(roomType));
    }

    public void handleDeleteRoomType(long id) {
        this.roomTypeRepository.deleteById(id);
    }

    public RoomType fetchRoomTypeById(long id) {
        return roomTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loại phòng với ID " + id + " không tồn tại"));
    }

    public List<RoomType> fetchAllRoomTypes() {
        return this.roomTypeRepository.findAll();
    }

    public RoomType handleUpdateRoomType(RoomType reqRoomType) {
        RoomType currentRoomType = this.fetchRoomTypeById(reqRoomType.getId());
        if (currentRoomType != null) {
            currentRoomType.setName(reqRoomType.getName());
            currentRoomType.setSize(reqRoomType.getSize());
            currentRoomType.setPrice(reqRoomType.getPrice());
            currentRoomType.setPeopleAbout(reqRoomType.getPeopleAbout());
        }
        return currentRoomType;
    }
}
