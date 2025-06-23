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
    public ResponseEntity<RoomResponse> createNewRoom(@RequestPart(name = "rooms") RoomRequest roomRequest, @RequestPart(name = "file", required = false)List<MultipartFile> multipartFiles) throws JsonProcessingException {
        System.out.println("rooms: " + roomRequest); // xem JSON tới chưa
        System.out.println("files: " + multipartFiles.size());

        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.handleCreateRoom(roomRequest, multipartFiles));
    }

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable("id") long id) {
        roomService.fetchRoomById(id);
        roomService.handleDeleteRoom(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomResponse>> getAllRoom() {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.fetchAllRooms());
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.fetchRoomById(id));
    }
    @PutMapping(value = "/rooms/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable("id") long  id, @RequestPart(name = "rooms") RoomUpdateRequest roomUpdateRequest, @RequestPart(name = "file", required = false)List<MultipartFile> multipartFiles) {
        System.out.println("rooms: " + roomUpdateRequest);
        System.out.println("room id: " + id);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.handleUpdateRoom(roomUpdateRequest,id, multipartFiles));
    }

    @GetMapping("/roomsinactive")
    public ApiResponse<List<Room>> getInactiveRooms(@RequestParam String roomType) {
        return new ApiResponse<>(200,null, roomService.getInactiveLuxuryRooms(roomType));
    }
    
}
