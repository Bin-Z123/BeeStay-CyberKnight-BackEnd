package com.poly.beestaycyberknightbackend.controller.client;

import java.util.Map;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.shaded.gson.Gson;
import com.poly.beestaycyberknightbackend.domain.Role;
import com.poly.beestaycyberknightbackend.domain.User;
import com.poly.beestaycyberknightbackend.dto.request.RegisterRequest;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.repository.RankRepository;
import com.poly.beestaycyberknightbackend.repository.UserRepository;
import com.poly.beestaycyberknightbackend.service.EmailService;
import com.poly.beestaycyberknightbackend.service.RedisService;
import com.poly.beestaycyberknightbackend.service.UserService;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RegisterController {

    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    RedisService redisService;
    EmailService emailService;
    UserService userService;
    private final RankRepository rankRepository;
    
    @PostMapping("/register/send-otp")
    public ApiResponse<Void> sendOtpForRegister(@RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ApiResponse.<Void>builder()
                .code(400)
                .message("Email đã được sử dụng")
                .build();
        }

        String json = new Gson().toJson(request); 
        redisService.saveOtp("register:" + request.getEmail(), json, 5); 

        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        redisService.saveOtp("otp:" + request.getEmail(), otp, 5);
        emailService.sendOtp(request.getEmail(), otp);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("OTP đã gửi về email")
                .build();
    }

    @PostMapping("/register/verify-otp")
    public ApiResponse<Void> verifyOtpAndRegister(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String otp = body.get("otp");

        if (email == null || otp == null) {
            return ApiResponse.<Void>builder()
                    .code(400)
                    .message("Thiếu thông tin")
                    .build();
        }

        String savedOtp = redisService.getOtp("otp:" + email);
        String userData = redisService.getOtp("register:" + email);

        if (savedOtp == null || !savedOtp.equals(otp) || userData == null) {
            return ApiResponse.<Void>builder()
                    .code(400)
                    .message("OTP sai, hết hạn hoặc thông tin đăng ký không tồn tại")
                    .build();
        }

        RegisterRequest registerRequest = new Gson().fromJson(userData, RegisterRequest.class);
        
        User user = new User();
        user.setFullname(registerRequest.getFirstName() + " " + registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        Role role = userService.getRoleByName("USER");
        user.setRole(role);

        user.setRank(rankRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Rank mặc định không tồn tại")));

        userRepository.save(user);

        redisService.deleteOtp("register:" + email);
        redisService.deleteOtp("otp:" + email);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Đăng ký thành công")
                .build();
    }


}
