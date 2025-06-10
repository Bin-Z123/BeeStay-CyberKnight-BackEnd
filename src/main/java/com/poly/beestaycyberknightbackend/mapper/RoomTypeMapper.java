package com.poly.beestaycyberknightbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.poly.beestaycyberknightbackend.domain.RoomType;
import com.poly.beestaycyberknightbackend.domain.dto.request.RoomTypeCreation;
import com.poly.beestaycyberknightbackend.domain.dto.response.RoomTypeResponse;

@Mapper(componentModel = "spring")
public interface RoomTypeMapper {
    RoomTypeMapper INSTANCE = Mappers.getMapper(RoomTypeMapper.class);
    RoomTypeResponse toRoomTypeResponse(RoomType roomType);
    RoomType toRoomType(RoomTypeCreation roomTypeCreation);
    void updateRoomType(RoomTypeCreation creation, @MappingTarget RoomType roomType);
}
