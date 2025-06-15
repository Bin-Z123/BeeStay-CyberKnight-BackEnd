package com.poly.beestaycyberknightbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.beestaycyberknightbackend.domain.Facility;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long>{
    boolean existsByFacilityName(String facilityName);
    boolean existsById(Long id);
}
