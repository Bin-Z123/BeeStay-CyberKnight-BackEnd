package com.poly.beestaycyberknightbackend.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Payment")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Integer amount;

    @Column(name = "payment_code", length = 100)
    String paymentCode;

    @Column(name = "payment_date")
    LocalDateTime paymentDate = LocalDateTime.now();

    @Column(name = "payment_method", nullable = false, length = 100)
    String paymentMethod;

    @Column(name = "payment_status", nullable = false, length = 10)
    String paymentStatus;

    @Column(name = "payment_type", nullable = false, length = 10)
    String paymentType;

    @Column(name = "raw_response")
    String rawResponse;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id")
    @JsonBackReference
    Booking booking;

}
