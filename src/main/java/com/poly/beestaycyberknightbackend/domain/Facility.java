package com.poly.beestaycyberknightbackend.domain;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Facilities")
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "facilityname", length = 200)
    String facilityName;

    @Column(name = "description", length = 255)
    String description;

    @Column(name = "price", precision = 18, scale = 2)
    BigDecimal price;

    @OneToMany(mappedBy = "facility")
    @JsonManagedReference
    List<BookingFacility> bookingFacilities;
}
