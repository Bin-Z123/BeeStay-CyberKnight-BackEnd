package com.poly.beestaycyberknightbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import com.poly.beestaycyberknightbackend.domain.User;
import com.poly.beestaycyberknightbackend.domain.User.EBlacklist;
import com.poly.beestaycyberknightbackend.dto.request.UserRequest;
import com.poly.beestaycyberknightbackend.dto.response.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toUser(UserRequest request);

    @Mapping(source = "EBlacklist", target = "EBlacklist", qualifiedByName = "mapEBlacklist")
    void updateUser(@MappingTarget User user, UserRequest request);

    @Named("mapEBlacklist")
    default EBlacklist mapEBlacklist(int idBlacklist) {
        return EBlacklist.values()[idBlacklist];
    }

    @Mapping(source = "EBlacklist", target = "EBlacklist", qualifiedByName = "mapEBlacklistToInt")
    UserResponse toUserResponse(User user);

    @Named("mapEBlacklistToInt")
    default int mapEBlacklistToInt(EBlacklist eBlacklist) {
        return eBlacklist.ordinal(); // Map EBlacklist enum về đúng giá trị int
    }

}
