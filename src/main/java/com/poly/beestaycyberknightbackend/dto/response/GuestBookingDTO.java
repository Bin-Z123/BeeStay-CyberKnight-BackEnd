package com.poly.beestaycyberknightbackend.dto.response;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GuestBookingDTO {

    Long id;

    String fullname;

    String phone;

    String email;

    String cccd;

    LocalDateTime createAt;
}
