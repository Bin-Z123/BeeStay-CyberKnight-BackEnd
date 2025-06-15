package com.poly.beestaycyberknightbackend.controller.admin;

import org.springframework.web.bind.annotation.RestController;

import com.poly.beestaycyberknightbackend.domain.Role;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.dto.response.RoleResponse;
import com.poly.beestaycyberknightbackend.service.RoleService;

import io.swagger.v3.core.model.ApiDescription;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/admin/role")
public class RoleController {
    RoleService roleService;

    @GetMapping("/list")
    public ApiResponse<List<RoleResponse>> getRoles() {
        ApiResponse response = new ApiResponse<>(200, null, roleService.getRoles());
        return response;
    }

    @GetMapping("/{id}")
    public ApiResponse<List<Role>> getRoleAndUser(@PathVariable Long id) {
        ApiResponse response = new ApiResponse<>(200, null, roleService.getRoleAndUser(id));
        return response;
    }
    
    
}
