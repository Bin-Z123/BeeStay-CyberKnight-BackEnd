package com.poly.beestaycyberknightbackend.controller.admin;

import org.springframework.web.bind.annotation.RestController;
import com.poly.beestaycyberknightbackend.domain.Role;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.dto.response.RoleResponse;
import com.poly.beestaycyberknightbackend.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoleController {
    RoleService roleService;

    @GetMapping("/office/role/list")
    public ApiResponse<List<RoleResponse>> getRoles() {
        ApiResponse response = new ApiResponse<>(200, "Lấy danh sách vai trò thành công", roleService.getRoles());
        return response;
    }

    @GetMapping("/office/role/{id}")
    public ApiResponse<List<Role>> getRoleAndUser(@PathVariable Long id) {
        ApiResponse response = new ApiResponse<>(200, "Lấy thông tin vai trò và người dùng thành công",
                roleService.getRoleAndUser(id));
        return response;
    }

}
