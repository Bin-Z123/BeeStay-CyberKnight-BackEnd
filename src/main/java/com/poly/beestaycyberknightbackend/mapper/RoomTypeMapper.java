package com.poly.beestaycyberknightbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.poly.beestaycyberknightbackend.domain.RoomType;
import com.poly.beestaycyberknightbackend.dto.request.RoomTypeRequest;
import com.poly.beestaycyberknightbackend.dto.response.RoomTypeResponse;

@Mapper(componentModel = "spring")
public interface RoomTypeMapper {
    RoomTypeMapper INSTANCE = Mappers.getMapper(RoomTypeMapper.class);

    @Mapping(target = "rooms", ignore = true)   
    RoomTypeResponse toRoomTypeResponse(RoomType roomType);
    RoomType toRoomType(RoomTypeRequest roomTypeRequest);
    void updateRoomType(RoomTypeRequest req, @MappingTarget RoomType roomType);
}
