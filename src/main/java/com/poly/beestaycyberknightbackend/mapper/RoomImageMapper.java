package com.poly.beestaycyberknightbackend.mapper;

import org.mapstruct.Mapper;
import com.poly.beestaycyberknightbackend.domain.RoomImage;
import com.poly.beestaycyberknightbackend.domain.dto.response.RoomImageResponse;

@Mapper(componentModel = "spring")
public interface RoomImageMapper {
    RoomImageResponse toRoomImageResponse(RoomImage image);
}
