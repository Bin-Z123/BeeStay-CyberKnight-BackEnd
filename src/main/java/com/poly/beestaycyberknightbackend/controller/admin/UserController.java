package com.poly.beestaycyberknightbackend.controller.admin;

import org.springframework.web.bind.annotation.RestController;
import com.poly.beestaycyberknightbackend.domain.User;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.dto.response.UserResponse;
import com.poly.beestaycyberknightbackend.service.UserService;
import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User postManUser) {
        String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
        postManUser.setPassword(hashPassword);
        User user = this.userService.handleCreateUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        if (id >= 10000) {
            throw new RuntimeException("Id không lớn hơn 10000");
        }
        userService.fetchUserById(id);
        userService.handleDeleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        User fetchUser = this.userService.fetchUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(fetchUser);
    }

    @GetMapping("/users")
    public ApiResponse<List<UserResponse>> getAllUser() {
        ApiResponse response = new ApiResponse<>(200, null, userService.fetchAllUser());
        return response;
    }

    @GetMapping("/")
    public String home() {
        return "hello";
    }

    @PutMapping("/updateUserRole/{userId}/{roleId}")
    public ApiResponse<User> updateUserRole(@PathVariable Long userId, @PathVariable Long roleId) {
        User updatedUser = userService.updateRoleforUser(userId, roleId);
        return new ApiResponse<>(200, null, updatedUser);
    }
}
