package com.poly.beestaycyberknightbackend.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatDTO {
    private String content;
    private long timestamp;
    private MessageType type;

    // Thông tin người gửi
    private long senderId;
    private String senderFullName;

    // Loại chat
    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    // Thông tin người nhận
    private long recipientId;
}
