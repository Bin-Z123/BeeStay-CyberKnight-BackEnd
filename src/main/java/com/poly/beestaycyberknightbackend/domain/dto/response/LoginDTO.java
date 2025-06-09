package com.poly.beestaycyberknightbackend.domain.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank(message = "Username không được bỏ trống")
    private String username;
    @NotBlank(message = "Password không được bỏ trống")
    private String password;
}
