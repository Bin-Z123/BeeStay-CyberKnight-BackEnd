package com.poly.beestaycyberknightbackend.controller.client;

import java.util.Map;
import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.poly.beestaycyberknightbackend.domain.User;
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
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || !userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("❌ Email không tồn tại!");
        }

        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        redisService.saveOtp(email, otp, 5);
        emailService.sendOtp(email, otp);
        return ResponseEntity.ok("✅ OTP đã gửi về email");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String otp = body.get("otp");
        if (email == null || otp == null) {
            return ResponseEntity.badRequest().body("❌ Thiếu thông tin email hoặc OTP");
        }

        String saved = redisService.getOtp(email);
        if (saved != null && saved.equals(otp)) {
            return ResponseEntity.ok("✅ OTP chính xác");
        }
        return ResponseEntity.badRequest().body("❌ OTP sai hoặc hết hạn");
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String otp = body.get("otp");
        String newPassword = body.get("newPassword");

        if (email == null || otp == null || newPassword == null) {
            return ResponseEntity.badRequest().body("❌ Thiếu thông tin");
        }

        String saved = redisService.getOtp(email);
        if (saved == null || !saved.equals(otp)) {
            return ResponseEntity.badRequest().body("❌ OTP sai hoặc hết hạn");
        }

        User user = userRepository.findByEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        redisService.deleteOtp(email);
        return ResponseEntity.ok("✅ Đổi mật khẩu thành công");
    }
}
