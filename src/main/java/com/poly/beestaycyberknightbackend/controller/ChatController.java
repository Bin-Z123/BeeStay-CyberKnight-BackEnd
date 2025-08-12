package com.poly.beestaycyberknightbackend.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.security.oauth2.jwt.Jwt;
import org.mapstruct.ap.internal.util.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.beestaycyberknightbackend.domain.User;
import com.poly.beestaycyberknightbackend.dto.ChatDTO;
import com.poly.beestaycyberknightbackend.dto.PrivateMessageRequestDTO;
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
        private final SimpMessagingTemplate messagingTemplate;

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

        @MessageMapping("/private-chat")
        public void sendPrivateMessage(Principal principal, @Payload PrivateMessageRequestDTO requestDTO) {
                User sender = userRepository.findByEmail(principal.getName());
                System.out.println(requestDTO);
                long recipientId = Long.parseLong(requestDTO.getRecipientId());
                userRepository.findById(recipientId).ifPresent(recipient -> {
                        if (recipient != null) {
                                // 1. Xây dựng đối tượng tin nhắn để gửi đi
                                ChatDTO chatDTO = ChatDTO.builder()
                                                .content(requestDTO.getContent())
                                                .timestamp(System.currentTimeMillis())
                                                .type(MessageType.CHAT)
                                                .senderId(sender.getId())
                                                .senderFullName(sender.getFullname())
                                                .recipientId(recipient.getId())
                                                .build();

                                // 2. Gửi tin nhắn đến người nhận
                                messagingTemplate.convertAndSendToUser(
                                                recipient.getEmail(),
                                                "/queue/messages",
                                                chatDTO);

                                // Gửi lại một bản sao cho chính người gửi để UI của họ cũng được cập nhật
                                messagingTemplate.convertAndSendToUser(
                                                principal.getName(),
                                                "/queue/messages",
                                                chatDTO);
                        }
                });

        }
}
