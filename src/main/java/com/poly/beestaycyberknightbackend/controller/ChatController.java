package com.poly.beestaycyberknightbackend.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.beestaycyberknightbackend.domain.User;
import com.poly.beestaycyberknightbackend.dto.ChatDTO;
import com.poly.beestaycyberknightbackend.dto.ChatDTO.MessageType;
import com.poly.beestaycyberknightbackend.repository.UserRepository;
import com.poly.beestaycyberknightbackend.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
// @RequestMapping("/api")
public class ChatController {
    // private final SecurityUtil securityUtil;
    private final UserRepository userRepository;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatDTO send(Principal principal, @Payload String message) {
        if (principal == null) {
            return ChatDTO.builder()
                    .content("Không thể xác thực người dùng!!")
                    .type(MessageType.LEAVE)
                    .build();
        }

        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        return ChatDTO.builder()
                .content(message)
                .timestamp(System.currentTimeMillis())
                .type(MessageType.CHAT)
                .senderId(user.getId()) // Giả sử User có getId()
                .senderFullName(user.getFullname())
                .build();
        // if (principal == null) {
        // return "User Person: " + message;
        // }
        // String email = principal.getName();
        // User user = userRepository.findByEmail(email);

        // String sender = (user != null) ? user.getFullname() : user.getEmail();

        // return sender + ": " + message;
    }

    @MessageMapping("/join")
    @SendTo("/topic/messages")
    public ChatDTO join(Principal principal, @Payload String message) {

        if (principal == null) {
            return ChatDTO.builder()
                    .content("Không thể xác thực người dùng!!")
                    .type(MessageType.LEAVE)
                    .build();
        }

        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        return ChatDTO.builder()
                .content(message)
                .timestamp(System.currentTimeMillis())
                .type(MessageType.JOIN)
                .senderId(user.getId()) // Giả sử User có getId()
                .senderFullName(user.getFullname())
                .build();
    }
}
