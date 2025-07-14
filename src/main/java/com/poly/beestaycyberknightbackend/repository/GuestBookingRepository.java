package com.poly.beestaycyberknightbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.beestaycyberknightbackend.domain.GuestBooking;
import java.util.List;


public interface GuestBookingRepository extends JpaRepository<GuestBooking, Long> {
    GuestBooking findByPhone(String phone);

}
