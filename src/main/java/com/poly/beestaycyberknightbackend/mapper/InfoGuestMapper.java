package com.poly.beestaycyberknightbackend.mapper;

import org.mapstruct.Mapper;

import com.poly.beestaycyberknightbackend.domain.InfoGuest;
import com.poly.beestaycyberknightbackend.dto.request.InfoGuestRequest;

@Mapper(componentModel = "spring")
public interface InfoGuestMapper {
    InfoGuest toEntity(InfoGuestRequest request);
}
