package com.poly.beestaycyberknightbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.poly.beestaycyberknightbackend.domain.Facility;
import com.poly.beestaycyberknightbackend.dto.request.FacilityRequest;
import com.poly.beestaycyberknightbackend.dto.response.FacilitiesDTO;

@Mapper(componentModel = "spring")
public interface FacilityMapper {
    Facility toFacility(FacilityRequest request);

    void updateFacility(@MappingTarget Facility facility, FacilityRequest request);

    FacilitiesDTO toFacilitiesDTO(Facility facility);
}
