package com.poly.beestaycyberknightbackend.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.beestaycyberknightbackend.domain.User;
import com.poly.beestaycyberknightbackend.repository.UserRepository;
import com.poly.beestaycyberknightbackend.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
// @RequestMapping("/api")
public class ChatController {
    private final SecurityUtil securityUtil;
    private final UserRepository userRepository;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public String send(Principal principal, String message) {

        if (principal == null) {
            return "User Person: " + message;
        }
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        String sender = (user != null) ? user.getFullname() : user.getEmail();

        return sender + ": " + message;
    }
}
