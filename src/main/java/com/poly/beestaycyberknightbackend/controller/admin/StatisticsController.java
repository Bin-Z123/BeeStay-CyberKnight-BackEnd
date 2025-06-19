package com.poly.beestaycyberknightbackend.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.service.StatisticsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;


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

    @GetMapping("/check-in-today-bookings")
    public ApiResponse<Long> getCheckInTodayBookings() {
        return new ApiResponse<>(200, null, statisticsService.getcheckInTodayBookings());
    }

    @GetMapping("/check-out-today-bookings")
    public ApiResponse<Long> getCheckOutTodayBookings() {
        return new ApiResponse<>(200, null, statisticsService.getCheckOutTodayBookings());
    }

    @GetMapping("/count-active-rooms")
    public ApiResponse<Long> getCountRoomActive() {
        return new ApiResponse<>(200, null, statisticsService.getCountRoomActive());
    }

    @GetMapping("/count-inactive-rooms")
    public ApiResponse<Long> getCountRoomInactive() {
        return new ApiResponse<>(200, null, statisticsService.getCountRoomInactive());
    }

}
