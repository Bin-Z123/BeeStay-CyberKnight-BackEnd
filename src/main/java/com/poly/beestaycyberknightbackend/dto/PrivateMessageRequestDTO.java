package com.poly.beestaycyberknightbackend.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrivateMessageRequestDTO {
    String content;
    String recipientId;
}
