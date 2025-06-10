package com.poly.beestaycyberknightbackend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.poly.beestaycyberknightbackend.domain.RoomType;
import com.poly.beestaycyberknightbackend.domain.dto.response.RestResponse;
import com.poly.beestaycyberknightbackend.service.RoomTypeService;
import com.poly.beestaycyberknightbackend.util.error.ResourceNotFoundException;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    
    @DeleteMapping("/roomTypes/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable("id") long id) {
        roomTypeService.fetchRoomTypeById(id);
        roomTypeService.handleDeleteRoomType(id);
        return ResponseEntity.noContent().build(); // Trả về 204 No Content, không có body
    }

    @GetMapping("/roomTypes")
    public ResponseEntity<List<RoomType>> getAllRoomType() {
        return ResponseEntity.status(HttpStatus.OK).body(this.roomTypeService.fetchAllRoomTypes());
    }

}
