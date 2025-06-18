package com.poly.beestaycyberknightbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.beestaycyberknightbackend.domain.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    long countByBookingStatus(String status);
    long count();
}
