package com.poly.beestaycyberknightbackend.controller.admin;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.beestaycyberknightbackend.dto.request.RoomUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poly.beestaycyberknightbackend.domain.Room;
import com.poly.beestaycyberknightbackend.dto.request.RoomRequest;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.dto.response.RoomResponse;
import com.poly.beestaycyberknightbackend.service.RoomService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class RoomController {
    
    private final RoomService roomService;

    @PostMapping(value = "/rooms",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RoomResponse> createNewRoom(@RequestPart(name = "rooms") RoomRequest roomRequest, 
    @RequestPart(name = "file", required = false)List<MultipartFile> multipartFiles) throws JsonProcessingException {
        System.out.println("rooms: " + roomRequest); // xem JSON tới chưa
        System.out.println("files: " + multipartFiles.size());
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.handleCreateRoom(roomRequest, multipartFiles));
    }

    @DeleteMapping("/rooms/{id}")
    public ApiResponse<Void> deleteRoom(@PathVariable("id") long id) {
        ApiResponse apuApiResponse = new ApiResponse<>();
        roomService.fetchRoomById(id);
        roomService.handleDeleteRoom(id);
        return new ApiResponse<>(204, null, null); // Trả về 204 No Content, không có body
    }

    @GetMapping("/rooms")
    public ApiResponse<List<RoomResponse>> getAllRoom() {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(roomService.fetchAllRooms());
        return apiResponse;
    }

    @GetMapping("/rooms/{id}")
    public ApiResponse<RoomResponse> getRoomById(@PathVariable("id") long id) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(roomService.fetchRoomById(id));
        return apiResponse;
    }
    @PutMapping(value = "/rooms/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<RoomResponse> updateRoom(@PathVariable("id") long  id, @RequestPart(name = "rooms") RoomUpdateRequest roomUpdateRequest, 
    @RequestPart(name = "file", required = false)List<MultipartFile> multipartFiles) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(201);
        apiResponse.setData(roomService.handleUpdateRoom(roomUpdateRequest, id, multipartFiles));
        return apiResponse;
        
        // System.out.println("rooms: " + roomUpdateRequest);
        // System.out.println("room id: " + id);
        // return ResponseEntity.status(HttpStatus.CREATED).body(roomService.handleUpdateRoom(roomUpdateRequest,id, multipartFiles));
    }

    @GetMapping("/roomsinactive")
    public ApiResponse<List<Room>> getInactiveRooms(@RequestParam String roomType) {
        return new ApiResponse<>(200,null, roomService.getInactiveLuxuryRooms(roomType));
    }
    
}
