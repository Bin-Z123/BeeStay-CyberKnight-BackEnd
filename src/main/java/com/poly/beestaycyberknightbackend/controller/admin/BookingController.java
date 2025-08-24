package com.poly.beestaycyberknightbackend.controller.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.poly.beestaycyberknightbackend.domain.Booking;
import com.poly.beestaycyberknightbackend.domain.BookingFacility;
import com.poly.beestaycyberknightbackend.domain.Facility;
import com.poly.beestaycyberknightbackend.dto.request.OrderBookingWrapper;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.dto.response.AvailableTypeRoomDTO;
import com.poly.beestaycyberknightbackend.dto.response.BookingDTO;
import com.poly.beestaycyberknightbackend.dto.response.BookingFacilitiesDTO;
import com.poly.beestaycyberknightbackend.dto.response.FacilitiesDTO;
import com.poly.beestaycyberknightbackend.exception.AppException;
import com.poly.beestaycyberknightbackend.exception.ErrorCode;
import com.poly.beestaycyberknightbackend.mapper.BookingFacilityMapper;
import com.poly.beestaycyberknightbackend.mapper.BookingMapper;
import com.poly.beestaycyberknightbackend.mapper.FacilityMapper;
import com.poly.beestaycyberknightbackend.repository.BookingFacilityRepository;
import com.poly.beestaycyberknightbackend.repository.BookingRepository;
import com.poly.beestaycyberknightbackend.repository.FacilityRepository;
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
    FacilityRepository facilityRepository;
    FacilityMapper facilityMapper;

    @GetMapping("/office/booking/list")
    public ApiResponse<List<BookingDTO>> getBookings() {
        return new ApiResponse<>(200, "Lấy danh sách thành công", bookingService.getAllBookings());
    }

    @PostMapping("/booking/order")
    public ApiResponse<BookingDTO> orderBooking(@RequestBody OrderBookingWrapper request) {
        try {
            Booking booking = bookingService.orderBooking(
                    request.getGuestBookingRequest(),
                    request.getBookingRequest(),
                    request.getBookingDetailRequest(),
                    request.getBookingFacilityRequest(),
                    request.getStayRequest());

            Booking booking2 = bookingRepository.findById(booking.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.BOOKINGDETAIL_NOT_EXISTED));
            BookingDTO bookingDTO = bookingMapper.toResponse(booking2);
            List<BookingFacility> bookingFacilities = bookingFacilityRepository.findByBookingId(booking2.getId());
            List<BookingFacilitiesDTO> bookingFacilitiesDTOs = bookingFacilities.stream()
                    .map(f -> {
                        BookingFacilitiesDTO bookingFacilitiesDTO = bookingFacilityMapper.toDto(f);
                        Optional<Facility> facilities = facilityRepository.findById(f.getFacility().getId());
                        List<FacilitiesDTO> facilitiesDTOs = facilities.map(facilityMapper::toFacilitiesDTO)
                                .stream().toList();
                        bookingFacilitiesDTO.setFacilities(facilitiesDTOs);
                        return bookingFacilitiesDTO;
                    }).collect(Collectors.toList());
            bookingDTO.setBookingFacilities(bookingFacilitiesDTOs);

            return new ApiResponse<>(HttpStatus.SC_OK, "đặt phòng thành công", bookingDTO);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.SC_BAD_REQUEST, e.getMessage(), null);
        }

    }

    @GetMapping("/rep/booking/bookingbycheckin")
    public ApiResponse<List<Booking>> getBookingsByGuest(@RequestParam LocalDate checkInDate) {
        return new ApiResponse<>(200, "Lấy danh sách thành công", bookingService.getBookingByCheckInDate(checkInDate));
    }

    @GetMapping("/office/booking/availableRoomsTypeAndDate")
    public ApiResponse<Long> countAvailableRooms(@RequestParam String nameRoomType, @RequestParam LocalDateTime date) {
        return new ApiResponse<>(200, "Tính toán số lượng phòng còn trống",
                bookingService.countAvailableRoomsByRoomTypeAndDate(nameRoomType, date));
    }

    @GetMapping("/availableRoomsTypeAndDateV2")
    public ApiResponse<List<AvailableTypeRoomDTO>> countAvailableRoomsV2(@RequestParam LocalDateTime fromDate,
            @RequestParam LocalDateTime toDate) {
        return new ApiResponse<>(200, "Lấy danh sách thành công", bookingService.getAvailableRooms(fromDate, toDate));
    }

    @GetMapping("/booking/{id}")
    public ApiResponse<BookingDTO> getBooking(@PathVariable Long id) {
        return new ApiResponse<>(200, "Lấy thông tin thành công", bookingService.getBooking(id));
    }

    // tính tiền ở thực tế
    @PutMapping("/update-booking/{id}")
    public ApiResponse<BookingDTO> updatePriceActual(@PathVariable Long id) {
        return new ApiResponse<>(200, "Cập nhật giá thành công", bookingService.updateTotalPriceBooking(id));
    }

    // tính tiền sau khi update booking detail
    @PutMapping("/afterUBD/{id}")
    public ApiResponse<BookingDTO> updatePriceAfterUpdateBD(@PathVariable Long id) {
        return new ApiResponse<>(HttpStatus.SC_OK, "Cập nhật giá thành công",
                bookingService.updateTotalPriceBookingAfter(id));
    }

    @PutMapping("/afterUBD2/{id}")
    public ApiResponse<BookingDTO> updatePriceAfterUpdateBD2(@PathVariable Long id) {
        return new ApiResponse<>(HttpStatus.SC_OK, "Cập nhật giá thành công",
                bookingService.updateTotalPriceBookingAfter2(id));
    }

    @PutMapping("/cancel/{id}")
    public ApiResponse<BookingDTO> cancelBooking(@PathVariable Long id) {
        return new ApiResponse<>(HttpStatus.SC_OK, "Hủy đặt phòng thành công",
                bookingService.setStatusBookingCancel(id));
    }

    @PutMapping("/checkout/{id}")
    public ApiResponse<?> checkoutBooking(@PathVariable Long id) {
        try {
            return new ApiResponse<>(HttpStatus.SC_OK, "Checkout thành công", bookingService.checkoutBookingStatus(id));
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.SC_BAD_REQUEST, "Insufficient payment, Please pay the full amount",
                    null);
        }

    }

    @PutMapping("/status-checkout/{id}")
    public ApiResponse<?> checkoutBookingCheckout(@PathVariable Long id) {
        try {
            return new ApiResponse<>(HttpStatus.SC_OK, "Checkout thành công",
                    bookingService.checkoutBookingStatusCheckout(id));
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.SC_BAD_REQUEST, "Insufficient payment, Please pay the full amount",
                    null);
        }

    }
}
