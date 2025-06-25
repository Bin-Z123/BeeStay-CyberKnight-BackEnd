package com.poly.beestaycyberknightbackend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Bookings")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "check_in_date", nullable = false)
    LocalDateTime checkInDate;

    @Column(name = "check_out_date", nullable = false)
    LocalDateTime checkOutDate;

    @Column(name = "total_amount", nullable = false)
    Integer totalAmount;

    @Column(name = "is_deposit", nullable = false)
    Boolean isDeposit;

    @Column(name = "e_booking_status", length = 10, nullable = false)
    String bookingStatus;

    @Column(name = "booking_date", nullable = false)
    LocalDateTime bookingDate = LocalDateTime.now();

    @Column(name = "num_guest", nullable = false)
    Integer numGuest;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "guest_id")
    GuestBooking guestBooking;

    @OneToMany(mappedBy = "booking", fetch = FetchType.EAGER)
    @JsonManagedReference
    List<BookingDetail> bookingDetails;
    
    @OneToMany(mappedBy = "booking", fetch = FetchType.EAGER)
    @JsonManagedReference
    List<BookingFacility> bookingFacilities;

    @OneToMany(mappedBy = "booking", fetch = FetchType.EAGER) 
    @JsonManagedReference
    List<Stay> stay;

    @Column(name = "numberofnights")
    Integer numberOfNights;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "review_id")
    // Review review;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "group_booking_id")
    // GroupBooking groupBooking;



}
