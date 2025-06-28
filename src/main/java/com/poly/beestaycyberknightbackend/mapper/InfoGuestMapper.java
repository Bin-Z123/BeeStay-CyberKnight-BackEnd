package com.poly.beestaycyberknightbackend.mapper;

import com.poly.beestaycyberknightbackend.domain.InfoGuest;
import com.poly.beestaycyberknightbackend.dto.response.InfoGuestResponse;
import com.poly.beestaycyberknightbackend.dto.request.InfoGuestRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InfoGuestMapper {
    InfoGuest toEntity(InfoGuestRequest request);
    InfoGuestResponse toResponse(InfoGuest guest);
}

