package com.poly.beestaycyberknightbackend.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "guestbooking")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GuestBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 100)
    String fullname;

    @Column(nullable = false, length = 15)
    String phone;

    @Column(nullable = false, length = 100)
    String email;

    @Column(nullable = false, length = 15)
    String cccd;

    @Column(name = "createat", nullable = false)
    LocalDateTime createAt = LocalDateTime.now();

    @OneToMany(mappedBy = "guestBooking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    List<Booking> bookings;
}