package com.poly.beestaycyberknightbackend.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poly.beestaycyberknightbackend.dto.request.RoomRequest;
import com.poly.beestaycyberknightbackend.dto.response.RoomResponse;
import com.poly.beestaycyberknightbackend.service.RoomService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class RoomController {
    
    private final RoomService roomService;

    @PostMapping("/rooms")
    public ResponseEntity<RoomResponse> createNewRoom(@RequestPart(name = "rooms") RoomRequest roomRequest, @RequestPart(name = "file", required = false)List<MultipartFile> multipartFiles) {
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
}
