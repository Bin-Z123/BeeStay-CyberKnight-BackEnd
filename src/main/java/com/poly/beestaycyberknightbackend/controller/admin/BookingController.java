package com.poly.beestaycyberknightbackend.controller.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.poly.beestaycyberknightbackend.domain.Booking;
import com.poly.beestaycyberknightbackend.domain.BookingFacility;
import com.poly.beestaycyberknightbackend.dto.request.OrderBookingWrapper;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.dto.response.AvailableTypeRoomDTO;
import com.poly.beestaycyberknightbackend.dto.response.BookingDTO;
import com.poly.beestaycyberknightbackend.dto.response.BookingFacilitiesDTO;
import com.poly.beestaycyberknightbackend.exception.AppException;
import com.poly.beestaycyberknightbackend.exception.ErrorCode;
import com.poly.beestaycyberknightbackend.mapper.BookingFacilityMapper;
import com.poly.beestaycyberknightbackend.mapper.BookingMapper;
import com.poly.beestaycyberknightbackend.repository.BookingFacilityRepository;
import com.poly.beestaycyberknightbackend.repository.BookingRepository;
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
@RequestMapping("/api")
public class BookingController {
    BookingService bookingService;
    BookingRepository bookingRepository;
    BookingMapper bookingMapper;
    BookingFacilityRepository bookingFacilityRepository;
    BookingFacilityMapper bookingFacilityMapper;

    @GetMapping("/admin/booking/list")
    public ApiResponse<List<BookingDTO>> getBookings() {
        return new ApiResponse<>(200, null, bookingService.getAllBookings());
    }

    @PostMapping("/admin/booking/order")
    public ApiResponse<BookingDTO> orderBooking(@RequestBody OrderBookingWrapper request) {
        try {
            Booking booking = bookingService.orderBooking(
                request.getGuestBookingRequest(),
                request.getBookingRequest(),
                request.getBookingDetailRequest(),
                request.getBookingFacilityRequest(),
                request.getStayRequest());
            
            Booking booking2 = bookingRepository.findById(booking.getId()).orElseThrow(() -> new AppException(ErrorCode.BOOKINGDETAIL_NOT_EXISTED));
            BookingDTO bookingDTO = bookingMapper.toResponse(booking2);
            List<BookingFacility> bookingFacilities = bookingFacilityRepository.findByBookingId(booking2.getId());
            List<BookingFacilitiesDTO> bookingFacilitiesDTOs = bookingFacilities.stream()
                    .map(bookingFacilityMapper::toDto).collect(Collectors.toList());
            bookingDTO.setBookingFacilities(bookingFacilitiesDTOs);

            return new ApiResponse<>(HttpStatus.SC_OK, null, bookingDTO);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.SC_BAD_REQUEST, e.getMessage(), null);
        }

        
    }

    @GetMapping("/admin/booking/bookingbycheckin")
    public ApiResponse<List<Booking>> getBookingsByGuest(@RequestParam LocalDate checkInDate) {
        return new ApiResponse<>(200, null, bookingService.getBookingByCheckInDate(checkInDate));
    }

    @GetMapping("/admin/booking/availableRoomsTypeAndDate")
    public ApiResponse<Long> countAvailableRooms(@RequestParam String nameRoomType, @RequestParam LocalDateTime date) {
        return new ApiResponse<>(200, null, bookingService.countAvailableRoomsByRoomTypeAndDate(nameRoomType, date));
    }

    @GetMapping("/availableRoomsTypeAndDateV2")
    public ApiResponse<List<AvailableTypeRoomDTO>> countAvailableRoomsV2(@RequestParam LocalDateTime fromDate,
            @RequestParam LocalDateTime toDate) {
        return new ApiResponse<>(200, null, bookingService.getAvailableRooms(fromDate, toDate));
    }


    @GetMapping("/booking/{id}")
    public ApiResponse<BookingDTO> getBooking(@PathVariable Long id) {
        return new ApiResponse<>(200, null, bookingService.getBooking(id));
    }


    // tính tiền ở thực tế
    @PutMapping("/update-booking/{id}")
    public ApiResponse<BookingDTO> updatePriceActual(@PathVariable Long id) {
        return new ApiResponse<>(200, null, bookingService.updateTotalPriceBooking(id));
    }

    // tính tiền sau khi update booking detail
    @PutMapping("/afterUBD/{id}")
    public ApiResponse<BookingDTO> updatePriceAfterUpdateBD(@PathVariable Long id) {
        return new ApiResponse<>(HttpStatus.SC_OK, null, bookingService.updateTotalPriceBookingAfter(id));
    }

    @PutMapping("/cancel/{id}")
    public ApiResponse<BookingDTO> cancelBooking(@PathVariable Long id) {
        return new ApiResponse<>(HttpStatus.SC_OK, null, bookingService.setStatusBookingCancel(id));
    }

    @PutMapping("/checkout/{id}")
    public ApiResponse<?> checkoutBooking(@PathVariable Long id) {
        try {
            return new ApiResponse<>(HttpStatus.SC_OK, null, bookingService.checkoutBookingStatus(id));
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.SC_BAD_REQUEST, "Insufficient payment, Please pay the full amount",
                    null);
        }

    }
}
