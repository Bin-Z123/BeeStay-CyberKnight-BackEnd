package com.poly.beestaycyberknightbackend.controller.client;

import java.util.Map;
import java.util.Random;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.poly.beestaycyberknightbackend.domain.User;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.repository.UserRepository;
import com.poly.beestaycyberknightbackend.service.EmailService;
import com.poly.beestaycyberknightbackend.service.RedisService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/forgot-password")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final RedisService redisService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/send-otp")
    public ApiResponse<Void> sendOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || !userRepository.existsByEmail(email)) {
            return ApiResponse.<Void>builder()
                    .code(400)
                    .message("Email không tồn tại!")
                    .build();
        }

        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        redisService.saveOtp(email, otp, 5); 
        emailService.sendOtp(email, otp);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("OTP đã gửi về email")
                .build();
    }

    @PostMapping("/verify-otp")
    public ApiResponse<Void> verifyOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String otp = body.get("otp");
        if (email == null || otp == null) {
            return ApiResponse.<Void>builder()
                    .code(400)
                    .message("Thiếu thông tin email hoặc OTP")
                    .build();
        }

        String saved = redisService.getOtp(email);
        if (saved != null && saved.equals(otp)) {
            return ApiResponse.<Void>builder()
                    .code(200)
                    .message("OTP chính xác")
                    .build();
        }

        return ApiResponse.<Void>builder()
                .code(400)
                .message("OTP sai hoặc đã hết hạn")
                .build();
    }

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String otp = body.get("otp");
        String newPassword = body.get("newPassword");

        if (email == null || otp == null || newPassword == null) {
            return ApiResponse.<Void>builder()
                    .code(400)
                    .message("Thiếu thông tin")
                    .build();
        }

        String saved = redisService.getOtp(email);
        if (saved == null || !saved.equals(otp)) {
            return ApiResponse.<Void>builder()
                    .code(400)
                    .message("OTP sai hoặc đã hết hạn")
                    .build();
        }

        User user = userRepository.findByEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        redisService.deleteOtp(email);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Đổi mật khẩu thành công")
                .build();
    }
}
