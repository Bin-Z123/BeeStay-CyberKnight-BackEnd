package com.poly.beestaycyberknightbackend.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.beestaycyberknightbackend.dto.request.BookingDetailUpdateRequest;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.service.BookingDetailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/admin/bookingDetail")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BookingDetailController {
    BookingDetailService bookingDetailService;

    @PutMapping("/{bookingId}")
    public ApiResponse<?> updateBookingDetail(@PathVariable Long bookingId, @RequestBody List<BookingDetailUpdateRequest> request) {
        
        return new ApiResponse<>(HttpStatus.OK.value(), null, bookingDetailService.updateBookingDetail(bookingId, request));
    }

}
