package com.poly.beestaycyberknightbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.beestaycyberknightbackend.domain.InfoGuest;

@Repository
public interface InfoGuestRepository extends JpaRepository<InfoGuest, Integer> {

    
}
