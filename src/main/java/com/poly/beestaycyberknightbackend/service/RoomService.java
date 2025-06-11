package com.poly.beestaycyberknightbackend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.poly.beestaycyberknightbackend.domain.Room;
import com.poly.beestaycyberknightbackend.domain.RoomImage;
import com.poly.beestaycyberknightbackend.domain.RoomType;
import com.poly.beestaycyberknightbackend.domain.dto.request.RoomRequest;
import com.poly.beestaycyberknightbackend.domain.dto.request.RoomTypeRequest;
import com.poly.beestaycyberknightbackend.domain.dto.response.RoomResponse;
import com.poly.beestaycyberknightbackend.mapper.RoomMapper;
import com.poly.beestaycyberknightbackend.repository.RoomRepository;
import com.poly.beestaycyberknightbackend.repository.RoomTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService {
    
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final RoomTypeRepository roomTypeRepository;

    public RoomResponse handleCreateRoom(RoomRequest request) {
    Room room = new Room();
    room.setRoomNumber(request.getRoomNumber());
    room.setRoomStatus(request.getRoomStatus());
    room.setFloor(request.getFloor());

    // ✅ Tìm RoomType nếu đã có, nếu chưa thì tạo mới
    RoomTypeRequest roomTypeReq = request.getRoomType();

    RoomType roomType = roomTypeRepository
        .findByNameAndSizeAndPriceAndPeopleAbout(
            roomTypeReq.getName(),
            roomTypeReq.getSize(),
            roomTypeReq.getPrice(),
            roomTypeReq.getPeopleAbout()
        )
        .orElseGet(() -> {
            // Tạo mới nếu không tìm thấy
            RoomType newType = new RoomType();
            newType.setName(roomTypeReq.getName());
            newType.setSize(roomTypeReq.getSize());
            newType.setPrice(roomTypeReq.getPrice());
            newType.setPeopleAbout(roomTypeReq.getPeopleAbout());
            return roomTypeRepository.save(newType); // Lưu trước để có ID
        });

    room.setRoomType(roomType);

    // ✅ Xử lý danh sách ảnh
    if (request.getRoomImages() != null) {
        List<RoomImage> images = request.getRoomImages().stream().map(imageRequest -> {
            RoomImage image = new RoomImage();
            image.setUrl(imageRequest.getUrl());
            image.setRoom(room); // Liên kết 2 chiều
            return image;
        }).collect(Collectors.toList());
        room.setRoomImages(images);
    }

    Room savedRoom = roomRepository.save(room);
    return roomMapper.toRoomResponse(savedRoom);
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
