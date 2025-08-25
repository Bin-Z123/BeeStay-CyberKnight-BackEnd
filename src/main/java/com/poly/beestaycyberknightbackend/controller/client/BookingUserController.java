package com.poly.beestaycyberknightbackend.controller.client;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.poly.beestaycyberknightbackend.dto.request.OrderBookingWrapper;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.service.BookingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class BookingUserController {
    BookingService bookingService;

    @PostMapping("/booking")
    public ApiResponse<?> orderBookingForUser(@RequestBody OrderBookingWrapper request) {
        try {
            Object object = bookingService.orderBookingForUser(request.getGuestBookingRequest(),
                    request.getBookingRequest(),
                    request.getBookingDetailRequest(), request.getBookingFacilityRequest(), request.getStayRequest());

            return new ApiResponse<>(HttpStatus.SC_OK, "success", object);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.SC_BAD_REQUEST, e.getMessage(), null);
        }
    }

}
