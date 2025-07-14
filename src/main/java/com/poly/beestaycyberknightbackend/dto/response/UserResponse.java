package com.poly.beestaycyberknightbackend.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    long id;
    String phone;
    String email;
    Boolean gender;
    LocalDate birthday;
    String password;
    LocalDateTime joinDate;
    String fullname;
    String cccd;
    int point;
    int eBlacklist;
    RoleResponse role;
    RankResponse rank;
}
