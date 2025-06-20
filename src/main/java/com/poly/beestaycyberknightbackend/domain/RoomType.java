package com.poly.beestaycyberknightbackend.domain;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.ManyToMany;

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
    private long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private int size;

    @Column(nullable = false)
    private int price;

    @Column(name = "people_about", nullable = false)
    private int peopleAbout;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Room> rooms;


    @ManyToMany(mappedBy = "roomTypes") // không cần tạo bảng trung gian nữa vì Discount đã làm rồi, giờ chỉ cần map qua.
    @JsonBackReference
    private List<Discount> discounts;

    @OneToMany(mappedBy = "roomType")
    @JsonManagedReference
    List<BookingDetail> bookingDetails;
}
