package com.poly.beestaycyberknightbackend.service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.poly.beestaycyberknightbackend.domain.Role;
import com.poly.beestaycyberknightbackend.dto.response.RoleResponse;
import com.poly.beestaycyberknightbackend.exception.AppException;
import com.poly.beestaycyberknightbackend.exception.ErrorCode;
import com.poly.beestaycyberknightbackend.mapper.RoleMapper;
import com.poly.beestaycyberknightbackend.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleService {
    RoleRepository repository;
    RoleMapper mapper;

    public List<RoleResponse> getRoles(){
        List<Role> list = repository.findAll();
        return list.stream().map(role -> mapper.toRoleResponse(role)).collect(Collectors.toList());
    }

    public Role getRoleAndUser(Long id){
        return repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
    }

}
