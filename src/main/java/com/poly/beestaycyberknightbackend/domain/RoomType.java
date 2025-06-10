package com.poly.beestaycyberknightbackend.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
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
@Table(name = "roomtypes")
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    long id;

    @Column(length = 50, nullable = false)
    String name;

    @Column(nullable = false)
    int size;

    @Column(nullable = false)
    int price;

    @Column(name = "people_about", nullable = false)
    int peopleAbout;

    @Column(name = "discount_id", nullable = false)
    int discountId;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Room> rooms;
}
