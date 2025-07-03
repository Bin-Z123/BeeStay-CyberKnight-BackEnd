package com.poly.beestaycyberknightbackend.controller.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.poly.beestaycyberknightbackend.domain.Booking;
import com.poly.beestaycyberknightbackend.dto.request.OrderBookingWrapper;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.dto.response.AvailableTypeRoomDTO;
import com.poly.beestaycyberknightbackend.dto.response.BookingDTO;
import com.poly.beestaycyberknightbackend.dto.response.BookingResponse;
import com.poly.beestaycyberknightbackend.service.BookingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/admin/booking")
public class BookingController {
    BookingService bookingService;

    @GetMapping("/list")
    public ApiResponse<List<BookingDTO>> getBookings() {
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

    @GetMapping("/bookingbycheckin")
    public ApiResponse<List<Booking>> getBookingsByGuest(@RequestParam LocalDate checkInDate) {
        return new ApiResponse<>(200, null, bookingService.getBookingByCheckInDate(checkInDate));
    }

    @GetMapping("/availableRoomsTypeAndDate")
    public ApiResponse<Long> countAvailableRooms(@RequestParam String nameRoomType, @RequestParam LocalDateTime date) {
        return new ApiResponse<>(200, null, bookingService.countAvailableRoomsByRoomTypeAndDate(nameRoomType, date));
    }

    @GetMapping("/availableRoomsTypeAndDateV2")
    public ApiResponse<List<AvailableTypeRoomDTO>> countAvailableRoomsV2(@RequestParam LocalDateTime fromDate, @RequestParam LocalDateTime toDate) {
        return new ApiResponse<>(200, null, bookingService.getAvailableRooms(fromDate, toDate));
    }
    
    @GetMapping("/{id}")
    public ApiResponse<BookingDTO>  getBooking(@PathVariable Long id) {
        return new ApiResponse<>(200, null, bookingService.getBooking(id));
    }
    
    @PutMapping("/{id}")
    public ApiResponse<BookingDTO>  updatePriceActual(@PathVariable Long id) {
        return new ApiResponse<>(200, null, bookingService.updateTotalPriceBooking(id));
    }
}
