package com.poly.beestaycyberknightbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.poly.beestaycyberknightbackend.domain.User;
import com.poly.beestaycyberknightbackend.domain.User.EBlacklist;
import com.poly.beestaycyberknightbackend.dto.request.UserRequest;
import com.poly.beestaycyberknightbackend.dto.response.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);

    @Mapping(source = "EBlacklist", target = "EBlacklist", qualifiedByName = "mapEBlacklist")
    void updateUser(@MappingTarget User user, UserRequest request);

    @Named("mapEBlacklist")
    default EBlacklist mapEBlacklist(int idBlacklist){
        return EBlacklist.values()[idBlacklist];
    }
    
}
