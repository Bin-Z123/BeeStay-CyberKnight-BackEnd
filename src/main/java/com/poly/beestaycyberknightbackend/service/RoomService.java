package com.poly.beestaycyberknightbackend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.poly.beestaycyberknightbackend.dto.request.RoomUpdateRequest;
import com.poly.beestaycyberknightbackend.repository.BookingRepository;
import com.poly.beestaycyberknightbackend.repository.RoomImageRepository;
import com.poly.beestaycyberknightbackend.util.CloudinaryUtil;
import org.springframework.stereotype.Service;

import com.poly.beestaycyberknightbackend.domain.Booking;
import com.poly.beestaycyberknightbackend.domain.Room;
import com.poly.beestaycyberknightbackend.domain.RoomImage;
import com.poly.beestaycyberknightbackend.domain.RoomType;
import com.poly.beestaycyberknightbackend.domain.Stay;
import com.poly.beestaycyberknightbackend.dto.request.RoomImageRequest;
import com.poly.beestaycyberknightbackend.dto.request.RoomRequest;
import com.poly.beestaycyberknightbackend.dto.request.RoomTypeRequest;
import com.poly.beestaycyberknightbackend.dto.response.BookingDTO;
import com.poly.beestaycyberknightbackend.dto.response.RoomResponse;
import com.poly.beestaycyberknightbackend.dto.response.StayDTO;
import com.poly.beestaycyberknightbackend.mapper.BookingMapper;
import com.poly.beestaycyberknightbackend.mapper.RoomMapper;
import com.poly.beestaycyberknightbackend.mapper.StayMapper;
import com.poly.beestaycyberknightbackend.repository.RoomRepository;
import com.poly.beestaycyberknightbackend.repository.RoomTypeRepository;
import com.poly.beestaycyberknightbackend.repository.StayRepository;

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
    private final RoomImageRepository roomImageRepository;
    private final StayRepository stayRepository;
    private final BookingRepository bookingRepository;
    private final StayMapper stayMapper;
    private final BookingMapper bookingMapper;

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

        String publicId = null;
        List<RoomImage> roomImages = new ArrayList<>();
        List<RoomImageRequest> roomImageRequests = roomRequest.getRoomImages();
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            for (int i = 0; i < roomImageRequests.size(); i++) {
                MultipartFile file = multipartFiles.get(i);
                if (file != null && !file.isEmpty()) {
                    Map uploadResult = cloudinaryUtil.uploadFile(file);
                    // imageUrl = (String) uploadResult.get("secure_url");
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
        }
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
        List<Room> listRoom = roomRepository.findAll();
        List<RoomResponse> listResp = listRoom.stream().map(list -> {
            RoomResponse roomResponse = roomMapper.toRoomResponse(list);

            List<Stay> listStay = stayRepository.findStayByRoomId(list.getId(), LocalDateTime.now());
            List<StayDTO> listStaydto = listStay.stream().map(listS -> {
                StayDTO staydto = stayMapper.toDto(listS);
                staydto.setRoomId(listS.getRoom().getId());
                return staydto;
            }).collect(Collectors.toList());

            List<Booking> listBooking = bookingRepository.findBookingByRoomId(list.getId(), LocalDateTime.now());
            List<BookingDTO> listBookingdto = listBooking.stream().map(listB -> {
                BookingDTO bookingdto = bookingMapper.toResponse(listB);
                return bookingdto;
            }).collect(Collectors.toList());

            roomResponse.setStay(listStaydto);
            roomResponse.setBooking(listBookingdto);

            return roomResponse;
        }).collect(Collectors.toList());

        return listResp;
    }

    @Transactional
    public RoomResponse handleUpdateRoom(RoomUpdateRequest roomUpdateRequest, long id, List<MultipartFile> files) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phòng với ID " + id + " không tồn tại"));
        // Xóa ảnh cũ
        for (String publicId : roomUpdateRequest.getDeletedRoomImageIds()) {
            cloudinaryUtil.deleteFile(publicId);
            roomImageRepository.deleteByUrl(publicId);
        }
        roomMapper.updateRoom(roomUpdateRequest, room);
        room.setRoomImages(new ArrayList<>());// Xóa list ảnh tạm thời để lưu phòng
        Room getSavedRoom = roomRepository.save(room);
        // Lưu RoomImages mới nếu có file
        String publicId = null;
        List<RoomImage> newRoomImages = new ArrayList<>();
        List<RoomImageRequest> roomImageRequests = roomUpdateRequest.getRoomImages(); // Lấy ảnh mới từ request
        if (files != null && !files.isEmpty()) {
            int fileIndex = 0;
            for (RoomImageRequest req : roomImageRequests) {
                // Bỏ qua ảnh cũ
                if (req.getUrl() != null && !req.getUrl().isBlank()) {
                    continue;
                }
                if (fileIndex >= files.size())
                    break;
                MultipartFile file = files.get(fileIndex++);
                if (file != null && !file.isEmpty()) {
                    Map uploadResults = cloudinaryUtil.uploadFile(file);
                    // imageUrl = (String) uploadResult.get("secure_url");
                    publicId = (String) uploadResults.get("public_id");

                    RoomImage image = new RoomImage();
                    image.setUrl(publicId);
                    image.setAltext(req.getAltext());
                    image.setIsThum(req.getIsThum());
                    image.setRoom(getSavedRoom); // GÁN ROOM TẠI ĐÂY
                    newRoomImages.add(image);
                }
            }
        }

        if (!newRoomImages.isEmpty()) {
            getSavedRoom.getRoomImages().addAll(newRoomImages);
        }
        return roomMapper.toRoomResponse(roomRepository.save(getSavedRoom));
    }

    public List<Room> getInactiveRooms(String roomType) {
        return roomRepository.findInactiveRoomsByRoomType(roomType);
    }

}
