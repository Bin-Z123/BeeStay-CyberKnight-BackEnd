package com.poly.beestaycyberknightbackend.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.service.StatisticsService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/admin/statistics")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StatisticsController {
    StatisticsService statisticsService;

    @GetMapping("/active-room-percentage")
    public ApiResponse<Double> getPercentRoomActive() {
        return new ApiResponse<>(200, null, statisticsService.getPercentRoomActive());
    }

    @GetMapping("/cancel-booking-percentage")
    public ApiResponse<Double> getPercentBookingCancel() {
        return new ApiResponse<>(200, null, statisticsService.getPercentBookingCancel());
    }
}
