package com.poly.beestaycyberknightbackend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.beestaycyberknightbackend.domain.Stay;

@Repository
public interface StayRepository extends JpaRepository<Stay, Integer> {
    
}
