package com.poly.beestaycyberknightbackend.dto.request;

import java.time.LocalDate;
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

public class UserUpdateRequest {
    String phone;
    String email;
    Boolean gender;
    LocalDate birthday;
    LocalDateTime updateDate = LocalDateTime.now();
    String fullname;
    int eBlacklist;
    String cccd;
    int point = 0;
    Long roleId;
    Integer rankId;
}
