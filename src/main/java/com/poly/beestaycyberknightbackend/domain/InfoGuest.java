package com.poly.beestaycyberknightbackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "infoguests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InfoGuest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(length = 15, nullable = false)
    String cccd;

    @Column(name = "occupant_type", length = 20, nullable = false)
    String occupantType;

    @Column(length = 50, nullable = false)
    String name;

    @Column(length = 15, nullable = false)
    String phone;

    @ManyToOne
    @JoinColumn(name = "stay_id", nullable = false)
    @JsonIgnore
    Stay stay;
}