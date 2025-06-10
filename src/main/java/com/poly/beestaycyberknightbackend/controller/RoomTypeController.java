package com.poly.beestaycyberknightbackend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.poly.beestaycyberknightbackend.domain.RoomType;
import com.poly.beestaycyberknightbackend.service.RoomTypeService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class RoomTypeController {
    
    private final RoomTypeService roomTypeService;

    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @PostMapping("/roomTypes")
    public ResponseEntity<RoomType> createNewRoomType (@RequestBody RoomType roomtypes) {
        RoomType roomType = this.roomTypeService.handleCreateRoomType(roomtypes);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomType);
    }    

}
