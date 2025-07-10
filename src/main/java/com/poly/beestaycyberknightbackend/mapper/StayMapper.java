package com.poly.beestaycyberknightbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.poly.beestaycyberknightbackend.domain.Stay;
import com.poly.beestaycyberknightbackend.dto.request.StayCreationRequest;
import com.poly.beestaycyberknightbackend.dto.request.StayRequest;
import com.poly.beestaycyberknightbackend.dto.response.StayDTO;

@Mapper(componentModel = "spring")
public interface StayMapper {
    Stay toEntity(StayRequest request);

    Stay toStay(StayCreationRequest request);

    StayDTO toDto(Stay stay);
}
