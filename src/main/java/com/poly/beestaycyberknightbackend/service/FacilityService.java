package com.poly.beestaycyberknightbackend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.poly.beestaycyberknightbackend.domain.Facility;
import com.poly.beestaycyberknightbackend.dto.request.FacilityRequest;
import com.poly.beestaycyberknightbackend.exception.AppException;
import com.poly.beestaycyberknightbackend.exception.ErrorCode;
import com.poly.beestaycyberknightbackend.mapper.FacilityMapper;
import com.poly.beestaycyberknightbackend.repository.FacilityRepository;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FacilityService {
    FacilityRepository facilityRepository;
    FacilityMapper mapper;

    public Facility createFacility(FacilityRequest request) {
        if (facilityRepository.existsByFacilityName(request.getFacilityName())) {
            throw new AppException(ErrorCode.FACILITIES_EXISTED);
        }
        Facility facility = mapper.toFacility(request);
        return facilityRepository.save(facility);
    }

    public List<Facility> getFacilities() {
        return facilityRepository.findAll();
    }

    public Facility getFacilityById(Long id) {
        return facilityRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FACILITIES_NOT_EXISTED));
    }

    public Facility updateFacility(Long id, FacilityRequest request) {

        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FACILITIES_NOT_EXISTED));
        mapper.updateFacility(facility, request);
        return facilityRepository.save(facility);
    }

    @Transactional
    public Facility deleteFacility(Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FACILITIES_NOT_EXISTED));
        facilityRepository.delete(facility);
        return facility;
    }

}
