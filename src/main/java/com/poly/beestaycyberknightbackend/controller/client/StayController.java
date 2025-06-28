package com.poly.beestaycyberknightbackend.controller.client;

import com.poly.beestaycyberknightbackend.dto.request.StayRequest;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.dto.response.StayResponse;
import com.poly.beestaycyberknightbackend.service.StayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class StayController {

    private final StayService stayService;

    @PostMapping(value = "/stays", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<StayResponse> createNewStay(@RequestBody StayRequest stayRequest) {
        StayResponse stayResponse = stayService.handleCreateStay(stayRequest);
        return new ApiResponse<>(201, null, stayResponse);
    }

    @GetMapping("/stays/{id}")
    public ApiResponse<StayResponse> getStayById(@PathVariable("id") int id) {
        StayResponse stay = stayService.fetchStayById(id);
        return new ApiResponse<>(200, null, stay);
    }

    @GetMapping("/stays")
    public ApiResponse<List<StayResponse>> getAllStays() {
        List<StayResponse> stays = stayService.fetchAllStays();
        return new ApiResponse<>(200, null, stays);
    }

    @DeleteMapping("/stays/{id}")
    public ApiResponse<Void> deleteStay(@PathVariable("id") int id) {
        stayService.fetchStayById(id); // check tồn tại
        stayService.handleDeleteStay(id);
        return new ApiResponse<>(204, null, null);
    }
}

