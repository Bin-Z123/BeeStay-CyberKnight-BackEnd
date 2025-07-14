package com.poly.beestaycyberknightbackend.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Stays")
public class Stay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @JsonBackReference
    Room room;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id")
    @JsonBackReference
    Booking booking;

    @Column(name = "actualcheckin", nullable = false)
    LocalDateTime actualCheckIn;

    @Column(name = "actualcheckout")
    LocalDateTime actualCheckOut;

    @Column(name = "staystatus", length = 30, nullable = false)
    String stayStatus;

    @Column(name = "create_at", nullable = false)
    LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "note", columnDefinition = "TEXT")
    String note;

    @OneToMany(mappedBy = "stay", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<InfoGuest> infoGuests;
}