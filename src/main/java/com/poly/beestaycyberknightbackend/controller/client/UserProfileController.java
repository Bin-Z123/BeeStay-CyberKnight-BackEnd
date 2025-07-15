package com.poly.beestaycyberknightbackend.controller.client;

import com.poly.beestaycyberknightbackend.dto.request.UserRequest;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.dto.response.UserResponse;
import com.poly.beestaycyberknightbackend.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserProfileController {

    private UserService userService;

    @PutMapping("/update-profile/{id}")
    public ApiResponse<UserResponse> updateProfile(@PathVariable Long id, @RequestBody UserRequest request) {
        UserResponse updatedUser = userService.updateUserProfile(id, request);
        return new ApiResponse<>(200, "Cập nhật thông tin thành công", updatedUser);
    }

    @GetMapping("/profile/{id}")
    public ApiResponse<UserResponse> getProfile(@PathVariable("id") long id) {
        UserResponse user = userService.fetchUserById(id);
        return new ApiResponse<>(200, "Lấy thông tin thành công", user);
    }
}
