package com.poly.beestaycyberknightbackend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InfoGuestResponse {
    int id;
    String cccd;
    String occupantType;
    String name;
    String phone;
}
