package com.poly.beestaycyberknightbackend.domain;

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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bookingfacilities")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingFacility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id")
    @JsonBackReference
    Booking booking;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    @JsonBackReference
    Facility facility;

    @Column(name = "quanlity")
    Integer quantity;
}
