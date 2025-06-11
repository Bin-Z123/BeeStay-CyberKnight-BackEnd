package com.poly.beestaycyberknightbackend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.poly.beestaycyberknightbackend.domain.RoomType;
import com.poly.beestaycyberknightbackend.dto.request.RoomTypeRequest;
import com.poly.beestaycyberknightbackend.dto.response.RoomTypeResponse;
import com.poly.beestaycyberknightbackend.exception.ResourceNotFoundException;
import com.poly.beestaycyberknightbackend.mapper.RoomTypeMapper;
import com.poly.beestaycyberknightbackend.repository.RoomTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomTypeService {
    
    private final RoomTypeRepository roomTypeRepository;
    private final RoomTypeMapper roomTypeMapper;

    public RoomTypeResponse handleCreateRoomType(RoomTypeRequest roomTypeRequest) {
        RoomType roomType = roomTypeMapper.toRoomType(roomTypeRequest);
        return  roomTypeMapper.toRoomTypeResponse(roomTypeRepository.save(roomType));
    }

    public void handleDeleteRoomType(long id) {
        this.roomTypeRepository.deleteById(id);
    }

    public RoomTypeResponse fetchRoomTypeById(long id) {
        return roomTypeRepository.findById(id)
                .map(roomTypeMapper::toRoomTypeResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Loại phòng với ID " + id + " không tồn tại"));
    }

    public List<RoomTypeResponse> fetchAllRoomTypes() {
        return roomTypeRepository.findAll()
                .stream()
                .map(roomTypeMapper::toRoomTypeResponse)
                .toList();
    }

    public RoomTypeResponse handleUpdateRoomType(RoomTypeRequest roomTypeRequest, long id) {
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loại phòng với ID " + id + " không tồn tại"));
                roomTypeMapper.updateRoomType(roomTypeRequest, roomType);
                return roomTypeMapper.toRoomTypeResponse(roomTypeRepository.save(roomType));
        
    }
}
