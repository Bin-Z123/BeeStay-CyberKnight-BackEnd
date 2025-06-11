package com.poly.beestaycyberknightbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.poly.beestaycyberknightbackend.domain.Room;
import com.poly.beestaycyberknightbackend.domain.dto.request.RoomRequest;
import com.poly.beestaycyberknightbackend.domain.dto.response.RoomResponse;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomTypeMapper INSTANCE = Mappers.getMapper(RoomTypeMapper.class);
    RoomResponse toRoomResponse(Room room); 
    Room toRoom(RoomRequest roomreq);
    void updateRoom(RoomRequest req, @MappingTarget Room room);
}
