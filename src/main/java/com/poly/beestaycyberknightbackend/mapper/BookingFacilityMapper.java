package com.poly.beestaycyberknightbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.poly.beestaycyberknightbackend.domain.BookingFacility;
import com.poly.beestaycyberknightbackend.dto.request.BookingFacilityRequest;
import com.poly.beestaycyberknightbackend.dto.response.BookingFacilitiesDTO;

@Mapper(componentModel = "spring")
public interface BookingFacilityMapper {
    BookingFacility toEntity(BookingFacilityRequest request);

    @Mapping(source = "booking.id", target = "bookingId")
    @Mapping(source = "facility.id", target = "facilityId")
    BookingFacilitiesDTO toDto(BookingFacility bookingFacility);
}
