package com.poly.beestaycyberknightbackend.dto.request;

import com.poly.beestaycyberknightbackend.service.validate.RegisterChecked;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterChecked
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    String firstName;
    String lastName;
    String email;
    String password;
    String confirmPassword;
}
