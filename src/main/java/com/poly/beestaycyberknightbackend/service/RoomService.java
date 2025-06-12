package com.poly.beestaycyberknightbackend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.poly.beestaycyberknightbackend.domain.Room;
import com.poly.beestaycyberknightbackend.domain.RoomImage;
import com.poly.beestaycyberknightbackend.domain.RoomType;
import com.poly.beestaycyberknightbackend.dto.request.RoomImageRequest;
import com.poly.beestaycyberknightbackend.dto.request.RoomRequest;
import com.poly.beestaycyberknightbackend.dto.response.RoomResponse;
import com.poly.beestaycyberknightbackend.mapper.RoomMapper;
import com.poly.beestaycyberknightbackend.repository.RoomRepository;
import com.poly.beestaycyberknightbackend.repository.RoomTypeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService {
    
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final RoomTypeRepository roomTypeRepository;

    @Transactional
    public RoomResponse handleCreateRoom(RoomRequest roomRequest) {
        Room room = new Room();
        room.setRoomNumber(roomRequest.getRoomNumber());
        room.setRoomStatus(roomRequest.getRoomStatus());
        room.setFloor(roomRequest.getFloor());

        // Gán RoomType
        RoomType roomType = roomTypeRepository.findById(roomRequest.getRoomTypeId())
            .orElseThrow(() -> new RuntimeException("RoomType not found"));
        room.setRoomType(roomType);

        // B1: Lưu Room trước để sinh ra ID
        Room savedRoom = roomRepository.save(room);

        // B2: Lưu RoomImages kèm theo Room đã có ID
        List<RoomImage> roomImages = new ArrayList<>();
        for (RoomImageRequest imageRequest : roomRequest.getRoomImages()) {
            RoomImage image = new RoomImage();
            image.setUrl(imageRequest.getUrl());
            image.setAltext(imageRequest.getAltext());
            image.setIsThum(imageRequest.getIsThum());
            image.setRoom(savedRoom); // GÁN ROOM TẠI ĐÂY
            roomImages.add(image);
        }

        // B3: Gán list RoomImages vào Room và lưu lại nếu cần cascade
        savedRoom.setRoomImages(roomImages);
        Room finalRoom = roomRepository.save(savedRoom);

        return roomMapper.toRoomResponse(finalRoom);
    }





    public void handleDeleteRoom(long id) {
        this.roomRepository.deleteById(id);
    }

    public RoomResponse fetchRoomById(long id) {
        return roomRepository.findById(id)
                .map(roomMapper::toRoomResponse)
                .orElseThrow(() -> new RuntimeException("Phòng với ID " + id + " không tồn tại"));
    }

    public List<RoomResponse> fetchAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(roomMapper::toRoomResponse)
                .toList();
    }

    public RoomResponse handleUpdateRoom(RoomRequest roomRequest, long id) {
        Room room = roomRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Phòng với ID " + id + " không tồn tại"));
        roomMapper.updateRoom(roomRequest, room);
        return roomMapper.toRoomResponse(roomRepository.save(room));
    }
}
