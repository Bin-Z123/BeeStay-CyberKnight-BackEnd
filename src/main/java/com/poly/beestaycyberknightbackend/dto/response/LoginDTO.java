package com.poly.beestaycyberknightbackend.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank(message = "Username không được bỏ trống")
    private String username;
    @NotBlank(message = "Password không được bỏ trống")
    private String password;
}
