package com.poly.beestaycyberknightbackend.mapper;

import org.mapstruct.Mapper;

import com.poly.beestaycyberknightbackend.domain.BookingFacility;
import com.poly.beestaycyberknightbackend.dto.request.BookingFacilityRequest;

@Mapper(componentModel = "spring")
public interface BookingFacilityMapper {
    BookingFacility toEntity(BookingFacilityRequest request);

}
