package com.poly.beestaycyberknightbackend.mapper;

import org.mapstruct.Mapper;

import com.poly.beestaycyberknightbackend.domain.Role;
import com.poly.beestaycyberknightbackend.dto.response.RoleResponse;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponse toRoleResponse(Role role);
}
