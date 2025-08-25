package com.poly.beestaycyberknightbackend.controller.admin;

import org.springframework.web.bind.annotation.RestController;
import com.poly.beestaycyberknightbackend.domain.User;
import com.poly.beestaycyberknightbackend.dto.request.UserRequest;
import com.poly.beestaycyberknightbackend.dto.request.UserUpdateRequest;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.dto.response.UserResponse;
import com.poly.beestaycyberknightbackend.service.UserService;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/admin/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<UserResponse> createNewUser(@RequestBody UserRequest userRequest) {
        ApiResponse response = new ApiResponse<>();
        String hashPassword = this.passwordEncoder.encode(userRequest.getPassword());
        userRequest.setPassword(hashPassword);
        response.setCode(201);
        response.setData(userService.handleCreateUser(userRequest));
        response.setMessage("Tạo người dùng thành công");
        return response;
    }

    @DeleteMapping("/admin/users/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable("id") long id) {
        if (id >= 10000) {
            throw new RuntimeException("Id không lớn hơn 10000");
        }
        userService.fetchUserById(id);
        userService.handleDeleteUser(id);
        return new ApiResponse<>(204, "Xóa người dùng thành công", null);
    }

    @GetMapping("/office/users/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable("id") long id) {
        ApiResponse response = new ApiResponse<>();
        response.setCode(200);
        response.setData(userService.fetchUserById(id));
        response.setMessage("Lấy thông tin người dùng thành công");
        return response;
    }

    @GetMapping("/office/users")
    public ApiResponse<List<UserResponse>> getAllUser() {
        ApiResponse response = new ApiResponse<>();
        response.setCode(200);
        response.setData(userService.fetchAllUser());
        response.setMessage("Lấy danh sách người dùng thành công");
        return response;
    }

    @PutMapping("/admin/updateUserRole/{userId}/{roleId}")
    public ApiResponse<User> updateUserRole(@PathVariable Long userId, @PathVariable Long roleId) {
        User updatedUser = userService.updateRoleforUser(userId, roleId);
        return new ApiResponse<>(200, "Cập nhật vai trò người dùng thành công", updatedUser);
    }

    @PutMapping("/admin/updateUser/{id}")
    public ApiResponse<User> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        return new ApiResponse<>(200, "Cập nhật thông tin người dùng thành công", userService.updateUser(id, request));
    }

    @PutMapping("/admin/updatePassword/{id}")
    public ApiResponse<User> upadatePassword(@PathVariable Long id, @RequestBody UserRequest request) {
        return new ApiResponse<>(200, "Cập nhật mật khẩu thành công", userService.updatePassword(id, request));
    }
}
