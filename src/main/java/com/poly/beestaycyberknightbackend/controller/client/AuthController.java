package com.poly.beestaycyberknightbackend.controller.client;


import java.time.LocalDate;
import java.time.LocalDateTime;


import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import com.poly.beestaycyberknightbackend.domain.Role;
import com.poly.beestaycyberknightbackend.domain.TransactionLog;
import com.poly.beestaycyberknightbackend.domain.User;
import com.poly.beestaycyberknightbackend.dto.request.RegisterRequest;
import com.poly.beestaycyberknightbackend.dto.request.RestLoginDTO;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.dto.response.LoginDTO;
import com.poly.beestaycyberknightbackend.repository.RankRepository;
import com.poly.beestaycyberknightbackend.repository.TransactionLogRepository;
import com.poly.beestaycyberknightbackend.repository.UserRepository;
import com.poly.beestaycyberknightbackend.service.UserService;
import com.poly.beestaycyberknightbackend.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthController {

    private final RankRepository rankRepository;

    private final PasswordEncoder passwordEncoder;
    
    AuthenticationManagerBuilder authenticationManagerBuilder;
    SecurityUtil securityUtil;
    TransactionLogRepository logRepository;
    UserRepository userRepository;
    UserService userService;

    // AuthController(PasswordEncoder passwordEncoder, RankRepository rankRepository) {
    //     this.passwordEncoder = passwordEncoder;
    //     this.rankRepository = rankRepository;
    // }
    @PostMapping("/login")
    public ResponseEntity<RestLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletRequest httpRequest) {

        //Nạp input gồm username & password vào Secủitycủity
        UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        //Xác thực người dùng -> Viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        
        //Create token
        String access_token = this.securityUtil.createToken(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);
 
        RestLoginDTO restLoginDTO = new RestLoginDTO();
        restLoginDTO.setAccessToken(access_token);
        User user = userRepository.findByEmail(loginDTO.getUsername());
        // Lưu log đăng nhập
        TransactionLog log = new TransactionLog();
        log.setActionType("LOGIN");
        log.setLogAt(LocalDateTime.now());
        log.setIp(httpRequest.getRemoteAddr());
        log.setAmount(0);
        log.setUser(user);
        logRepository.save(log);
      
        return ResponseEntity.ok().body(restLoginDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> handleRegister(@RequestBody @Valid RegisterRequest registerRequest) {

        // Check email đã tồn tại

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>builder()
                    .message("Email đã được sử dụng")
                    .code(400)
                    .build());
        }


        // Tạo User entity từ RegisterRequest

        User user = new User();
        user.setFullname(registerRequest.getFirstName() + " " + registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setPhone("0000000000"); // default
        user.setGender(true); // default
        // user.setBirthday(LocalDate.now().minusYears(18)); // default: đủ tuổi
        // user.setJoinDate(LocalDateTime.now());
        // user.setUpdateDate(LocalDateTime.now());
        // user.setEBlacklist(User.EBlacklist.NORM);
        user.setCccd("000000000000"); // default CCCD
        user.setPoint(0);


        // Gán Role mặc định USER
        Role role = userService.getRoleByName("USER");
        user.setRole(role);

        // Gán Rank mặc định (giả sử ID = 1)

        user.setRank(rankRepository.findById(1).orElseThrow(() -> new RuntimeException("Rank mặc định không tồn tại")));

        userRepository.save(user);

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Đăng ký thành công")
                .code(200)
                .build());
    }


}
