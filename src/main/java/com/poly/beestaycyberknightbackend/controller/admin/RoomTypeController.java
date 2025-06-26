package com.poly.beestaycyberknightbackend.controller.admin;

import org.springframework.web.bind.annotation.RestController;
import com.poly.beestaycyberknightbackend.domain.RoomType;
import com.poly.beestaycyberknightbackend.dto.request.RoomTypeRequest;

import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;

import com.poly.beestaycyberknightbackend.dto.response.RoomTypeResponse;
import com.poly.beestaycyberknightbackend.service.RoomTypeService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class RoomTypeController {
    
    private final RoomTypeService roomTypeService;

    @PostMapping("/roomTypes")

    public ApiResponse<RoomTypeResponse> createNewRoomType (@RequestBody RoomTypeRequest roomTypeRequest) {
        ApiResponse response = new ApiResponse<>();
        response.setCode(201);
        response.setData(roomTypeService.handleCreateRoomType(roomTypeRequest));
        return response;

    } 
    
    @DeleteMapping("/roomTypes/{id}")
    public ApiResponse<Void> deleteRoomType(@PathVariable("id") long id) {
        roomTypeService.fetchRoomTypeById(id);
        roomTypeService.handleDeleteRoomType(id);
        return new ApiResponse<>(204,null,null); // Trả về 204 No Content, không có body
    }

    @GetMapping("/roomTypes")
    public ApiResponse<List<RoomTypeResponse>> getAllRoomType() {
        ApiResponse response = new ApiResponse<>();
        response.setCode(200);
        response.setData(roomTypeService.fetchAllRoomTypes());
        return response;
    }

    @GetMapping("/roomTypes/{id}")
    public ApiResponse<RoomTypeResponse> getRoomTypeById(@PathVariable("id") long id) {
        ApiResponse response = new ApiResponse<>();
        response.setCode(200);
        response.setData(roomTypeService.fetchRoomTypeById(id));
        return response;
    }

    @PutMapping("/roomTypes/{id}")
    public ResponseEntity<RoomTypeResponse> handleUpdateRoomType (@PathVariable("id") long id, @RequestBody RoomTypeRequest roomTypeRequest) {
        return ResponseEntity.ok(roomTypeService.handleUpdateRoomType(roomTypeRequest, id));
    }
}

    


