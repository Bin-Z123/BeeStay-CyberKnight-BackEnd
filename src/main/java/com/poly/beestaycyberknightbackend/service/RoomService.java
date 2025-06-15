package com.poly.beestaycyberknightbackend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.poly.beestaycyberknightbackend.util.CloudinaryUtil;
import org.springframework.stereotype.Service;

import com.poly.beestaycyberknightbackend.domain.Room;
import com.poly.beestaycyberknightbackend.domain.RoomImage;
import com.poly.beestaycyberknightbackend.domain.RoomType;
import com.poly.beestaycyberknightbackend.dto.request.RoomImageRequest;
import com.poly.beestaycyberknightbackend.dto.request.RoomRequest;
import com.poly.beestaycyberknightbackend.dto.request.RoomTypeRequest;
import com.poly.beestaycyberknightbackend.dto.response.RoomResponse;
import com.poly.beestaycyberknightbackend.mapper.RoomMapper;
import com.poly.beestaycyberknightbackend.repository.RoomRepository;
import com.poly.beestaycyberknightbackend.repository.RoomTypeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RoomService {
    
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final RoomTypeRepository roomTypeRepository;
    private final CloudinaryUtil cloudinaryUtil;

    @Transactional
public RoomResponse handleCreateRoom(RoomRequest roomRequest, List<MultipartFile> multipartFiles) {
    Room room = new Room();
    room.setRoomNumber(roomRequest.getRoomNumber());
    room.setRoomStatus(roomRequest.getRoomStatus());
    int floor = roomRequest.getFloor();
    if (floor > 8 || floor < 1) {
        throw new RuntimeException("Floor must be between 1 and 8");
    }
    room.setFloor(roomRequest.getFloor());

    // Gán RoomType
    RoomType roomType = roomTypeRepository.findById(roomRequest.getRoomTypeId())
        .orElseThrow(() -> new RuntimeException("RoomType not found"));
    room.setRoomType(roomType);

    // B1: Lưu Room trước để sinh ra ID
    Room savedRoom = roomRepository.save(room);

    // B2: Lưu RoomImages kèm theo Room đã có ID
//        String imageUrl = null;
        String publicId = null;
    List<RoomImage> roomImages = new ArrayList<>();
    List<RoomImageRequest> roomImageRequests = roomRequest.getRoomImages();

    for (int i = 0 ; i < roomImageRequests.size(); i++) {
        MultipartFile file = multipartFiles.get(i);
        if (file != null && !file.isEmpty()){
            Map uploadResult = cloudinaryUtil.uploadFile(file);
//            imageUrl = (String) uploadResult.get("secure_url");
            publicId = (String) uploadResult.get("public_id");

            RoomImage image = new RoomImage();
            image.setUrl(publicId);
            image.setAltext(roomImageRequests.get(i).getAltext());
            image.setIsThum(roomImageRequests.get(i).getIsThum());
            image.setRoom(savedRoom); // GÁN ROOM TẠI ĐÂY
            roomImages.add(image);
        }

    }

    // B3: Gán list RoomImages vào Room và lưu lại nếu cần cascade
    savedRoom.setRoomImages(roomImages);

    return roomMapper.toRoomResponse(roomRepository.save(savedRoom));
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
