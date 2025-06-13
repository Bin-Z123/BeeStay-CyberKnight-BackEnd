package com.poly.beestaycyberknightbackend.controller.client;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import com.poly.beestaycyberknightbackend.domain.TransactionLog;
import com.poly.beestaycyberknightbackend.domain.User;
import com.poly.beestaycyberknightbackend.dto.request.RestLoginDTO;
import com.poly.beestaycyberknightbackend.dto.response.LoginDTO;
import com.poly.beestaycyberknightbackend.repository.TransactionLogRepository;
import com.poly.beestaycyberknightbackend.repository.UserRepository;
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
    
    AuthenticationManagerBuilder authenticationManagerBuilder;
    SecurityUtil securityUtil;
    TransactionLogRepository logRepository;
    UserRepository userRepository;



    @PostMapping("/login")
    public ResponseEntity<RestLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletRequest httpRequest) {
        //Nạp input gồm username & password vào Secủitycủity
        UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        //Xác thực người dùng -> Viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        
        //Create token
        String access_token = this.securityUtil.createToken(authentication);
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
}
