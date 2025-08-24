package com.poly.beestaycyberknightbackend.controller.client;

import com.poly.beestaycyberknightbackend.domain.User;
import com.poly.beestaycyberknightbackend.dto.request.UserRequest;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.dto.response.BookingDTO;
import com.poly.beestaycyberknightbackend.dto.response.BookingResponse;
import com.poly.beestaycyberknightbackend.dto.response.UserResponse;
import com.poly.beestaycyberknightbackend.repository.UserRepository;
import com.poly.beestaycyberknightbackend.service.BookingService;
import com.poly.beestaycyberknightbackend.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserProfileController {

    UserService userService;
    UserRepository userRepository;
    BookingService bookingService;

    @PutMapping("/update-profile")
    public ApiResponse<UserResponse> updateProfile(@RequestBody UserRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse updatedUser = userService.updateUserProfileByEmail(email, request);
        return new ApiResponse<>(200, "Cập nhật thông tin thành công", updatedUser);
    }
    @GetMapping("/profile")
    public ApiResponse<UserResponse> getProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse user = userService.fetchUserByEmail(email);
        return new ApiResponse<>(200, "Lấy thông tin thành công", user);
    }


    @GetMapping("/booking-history")
    public ApiResponse<List<BookingDTO>> getBookingHistory() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email);

        if (currentUser == null) {
            return new ApiResponse<>(404, "Không tìm thấy người dùng", null);
        }

        List<BookingDTO> bookings = bookingService.fetchBookingByUser(currentUser);
        return new ApiResponse<>(200, "Lấy thông tin thành công", bookings);
    }

    @GetMapping("/booking/{id}")
    public ApiResponse<BookingDTO> getBookingById(@PathVariable("id") long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUsser = userRepository.findByEmail(email);
        BookingDTO booking = bookingService.getBookingByIdAndUser(id, currentUsser);
        if (booking == null) {
            return new ApiResponse<>(404, "Không tìm thấy đơn đặt phòng", null);
        }
        return new ApiResponse<>(200, "Lấy thông tin thành công",booking);
    }
    
    
}