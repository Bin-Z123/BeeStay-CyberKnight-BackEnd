package com.poly.beestaycyberknightbackend.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.poly.beestaycyberknightbackend.domain.dto.request.RoomRequest;
import com.poly.beestaycyberknightbackend.domain.dto.response.RoomResponse;
import com.poly.beestaycyberknightbackend.service.RoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RoomController {
    
    private final RoomService roomService;

    @PostMapping("/rooms")
    public ResponseEntity<RoomResponse> createNewRoom(@RequestBody RoomRequest roomRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.handleCreateRoom(roomRequest));
    }

}
