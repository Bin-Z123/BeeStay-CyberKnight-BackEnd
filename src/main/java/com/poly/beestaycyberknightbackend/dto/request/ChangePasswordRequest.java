package com.poly.beestaycyberknightbackend.dto.request;

import com.poly.beestaycyberknightbackend.service.validate.StrongPassword;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @NotBlank
    String oldPassword;
    @NotBlank
    @StrongPassword
    String newPassword;
    @NotBlank
    String confimPassword;
}
