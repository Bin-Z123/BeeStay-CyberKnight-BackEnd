package com.poly.beestaycyberknightbackend.mapper;

import com.poly.beestaycyberknightbackend.domain.Stay;
import com.poly.beestaycyberknightbackend.dto.request.StayRequest;
import com.poly.beestaycyberknightbackend.dto.response.StayResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
    RoomMapper.class,
    BookingMapper.class,
    InfoGuestMapper.class
})
public interface StayMapper {
    Stay toEntity(StayRequest request);
    StayResponse toStayResponse(Stay stay);
}

