package com.poly.beestaycyberknightbackend.controller.admin;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.beestaycyberknightbackend.domain.Booking;
import com.poly.beestaycyberknightbackend.dto.request.BookingDetailRequest;
import com.poly.beestaycyberknightbackend.dto.request.BookingFacilityRequest;
import com.poly.beestaycyberknightbackend.dto.request.BookingRequest;
import com.poly.beestaycyberknightbackend.dto.request.GuestBookingRequest;
import com.poly.beestaycyberknightbackend.dto.request.InfoGuestRequest;
import com.poly.beestaycyberknightbackend.dto.request.OrderBookingWrapper;
import com.poly.beestaycyberknightbackend.dto.request.StayRequest;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.service.BookingService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/admin/booking")
public class BookingController {
    BookingService bookingService;

    @GetMapping("/list")
    public ApiResponse<List<Booking>> getBookings() {
        return new ApiResponse<>(200, null, bookingService.getAllBookings());
    }

    @PostMapping("/order")
    public ApiResponse<Booking> orderBooking(@RequestBody OrderBookingWrapper request) {
        return new ApiResponse<>(200, null, bookingService.orderBooking(
                request.getGuestBookingRequest(),
                request.getBookingRequest(),
                request.getBookingDetailRequest(),
                request.getBookingFacilityRequest(),
                request.getStayRequest()));
    }

}
