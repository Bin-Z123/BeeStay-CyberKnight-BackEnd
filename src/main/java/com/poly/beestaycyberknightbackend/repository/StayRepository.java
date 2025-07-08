package com.poly.beestaycyberknightbackend.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.beestaycyberknightbackend.domain.Stay;

@Repository
public interface StayRepository extends JpaRepository<Stay, Integer> {
    @Query("SELECT s FROM Stay s WHERE s.booking.id = :bId")
    List<Stay> listStayOfBooking(@Param("bId") Long bId);
}
