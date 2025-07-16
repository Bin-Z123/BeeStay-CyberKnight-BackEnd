package com.poly.beestaycyberknightbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.poly.beestaycyberknightbackend.domain.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findFirstByBookingIdAndPaymentStatusOrderByPaymentDateDesc(Long bookingId, String status);
}
