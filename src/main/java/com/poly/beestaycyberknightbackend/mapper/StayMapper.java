package com.poly.beestaycyberknightbackend.mapper;

import org.mapstruct.Mapper;

import com.poly.beestaycyberknightbackend.domain.Stay;
import com.poly.beestaycyberknightbackend.dto.request.StayCreationRequest;
import com.poly.beestaycyberknightbackend.dto.request.StayRequest;

@Mapper(componentModel = "spring")
public interface StayMapper {
    Stay toEntity(StayRequest request);

    Stay toStay(StayCreationRequest request);
}
