package com.poly.beestaycyberknightbackend.controller.client;


import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import com.poly.beestaycyberknightbackend.domain.Rank;
import com.poly.beestaycyberknightbackend.domain.Role;
import com.poly.beestaycyberknightbackend.domain.TransactionLog;
import com.poly.beestaycyberknightbackend.domain.User;
import com.poly.beestaycyberknightbackend.dto.request.ChangePasswordRequest;
import com.poly.beestaycyberknightbackend.dto.request.RegisterRequest;
import com.poly.beestaycyberknightbackend.dto.request.RestLoginDTO;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.dto.response.CustomUserDetails;
import com.poly.beestaycyberknightbackend.dto.response.LoginDTO;
import com.poly.beestaycyberknightbackend.dto.response.RankResponse;
import com.poly.beestaycyberknightbackend.dto.response.RoleResponse;
import com.poly.beestaycyberknightbackend.dto.response.UserResponse;
import com.poly.beestaycyberknightbackend.repository.RankRepository;
import com.poly.beestaycyberknightbackend.repository.TransactionLogRepository;
import com.poly.beestaycyberknightbackend.repository.UserRepository;
import com.poly.beestaycyberknightbackend.service.UserService;
import com.poly.beestaycyberknightbackend.util.SecurityUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api")
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
    public ApiResponse<RestLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO,
                                        HttpServletRequest httpRequest,
                                        HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        String access_token = this.securityUtil.createToken(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // ✅ Thêm cookie chứa token
        Cookie cookie = new Cookie("jwt", access_token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // nên là true nếu dùng HTTPS thực tế
        cookie.setPath("/");
        cookie.setMaxAge(30 * 60); // 30 phút

        response.addCookie(cookie);

        // ✅ Để frontend dùng nếu cần
        RestLoginDTO restLoginDTO = new RestLoginDTO();
        restLoginDTO.setAccessToken(access_token);

        // ✅ Ghi log đăng nhập
        User user = userRepository.findByEmail(loginDTO.getUsername());
        TransactionLog log = new TransactionLog();
        log.setActionType("LOGIN");
        log.setLogAt(LocalDateTime.now());
        log.setIp(httpRequest.getRemoteAddr());
        log.setAmount(0);
        log.setUser(user);
        logRepository.save(log);

        return ApiResponse.<RestLoginDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Đăng nhập thành công")
                .data(restLoginDTO)
                .build();
    }



    // @PostMapping("/register")
    // public ApiResponse<Void> handleRegister(@RequestBody @Valid RegisterRequest registerRequest) {
    //     if (userRepository.existsByEmail(registerRequest.getEmail())) {
    //         return ApiResponse.<Void>builder()
    //                 .message("Email đã được sử dụng")
    //                 .code(HttpStatus.BAD_REQUEST.value())
    //                 .build();
    //     }

    //     User user = new User();
    //     user.setFullname(registerRequest.getFirstName() + " " + registerRequest.getLastName());
    //     user.setEmail(registerRequest.getEmail());
    //     user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

    //     // ✅ Bạn có thể bật lại các giá trị mặc định khi cần
    //     // user.setPhone("0000000000");
    //     // user.setGender(true);
    //     // user.setBirthday(LocalDate.now().minusYears(18));
    //     // user.setJoinDate(LocalDateTime.now());
    //     // user.setUpdateDate(LocalDateTime.now());
    //     // user.setEBlacklist(User.EBlacklist.NORM);
    //     // user.setCccd("000000000000");
    //     // user.setPoint(0);

    //     Role role = userService.getRoleByName("USER");
    //     user.setRole(role);

    //     user.setRank(rankRepository.findById(1)
    //             .orElseThrow(() -> new RuntimeException("Rank mặc định không tồn tại")));

    //     userRepository.save(user);

    //     return ApiResponse.<Void>builder()
    //             .message("Đăng ký thành công")
    //             .code(HttpStatus.OK.value())
    //             .build();
    // }

    


    @PostMapping("/change_password")
    public ApiResponse<Void> changePassword(@RequestBody ChangePasswordRequest request, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdateDate(LocalDateTime.now());
        userRepository.save(user);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Đổi mật khẩu thành công")
                .build();
    }


    @GetMapping("/me")
    public ApiResponse<UserResponse> getCurrentUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return ApiResponse.<UserResponse>builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("Chưa đăng nhập")
                    .build();
        }

        Cookie jwtCookie = Arrays.stream(cookies)
                .filter(cookie -> "jwt".equals(cookie.getName()))
                .findFirst()
                .orElse(null);

        if (jwtCookie == null) {
            return ApiResponse.<UserResponse>builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("Chưa đăng nhập")
                    .build();
        }

        try {
            // ✅ Giải mã JWT từ cookie
            Jwt jwt = securityUtil.decodeToken(jwtCookie.getValue());
            String username = jwt.getSubject();

            // ✅ Tìm user từ DB
            User user = userRepository.findByEmail(username);
            if (user == null) {
                return ApiResponse.<UserResponse>builder()
                        .code(HttpStatus.UNAUTHORIZED.value())
                        .message("Tài khoản không tồn tại")
                        .build();
            }

            // ✅ Mapping dữ liệu
            RoleResponse roleRes = new RoleResponse();
            roleRes.setId(user.getRole().getId());
            roleRes.setRoleName(user.getRole().getRoleName());

            RankResponse rankRes = null;
            if (user.getRank() != null) {
                rankRes = new RankResponse();
                rankRes.setId(user.getRank().getId());
                rankRes.setNameRank(user.getRank().getNameRank());
            }

            UserResponse response = new UserResponse();
            response.setId(user.getId());
            response.setPhone(user.getPhone());
            response.setEmail(user.getEmail());
            response.setGender(user.getGender());
            response.setBirthday(user.getBirthday());
            response.setJoinDate(user.getJoinDate());
            response.setFullname(user.getFullname());
            response.setCccd(user.getCccd());
            response.setPoint(user.getPoint());
            response.setRole(roleRes);
            response.setRank(rankRes);

            return ApiResponse.<UserResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Lấy thông tin người dùng thành công")
                    .data(response)
                    .build();

        } catch (Exception e) {
            return ApiResponse.<UserResponse>builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("Token không hợp lệ hoặc đã hết hạn")
                    .build();
        }
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // xoá cookie
        response.addCookie(cookie);

        return ApiResponse.<Void>builder()
                .message("Đăng xuất thành công")
                .code(200)
                .build();
    }








}
