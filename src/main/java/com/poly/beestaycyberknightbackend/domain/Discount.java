package com.poly.beestaycyberknightbackend.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "title")
    String title;

    @Column(name = "description")
    String description;

    @Column(name = "discountcode")
    String discountCode;

    @Column(name = "discounttype")
    String discountType;

    @Column(name = "discountvalue")
    String discountValue;

    @Column(name = "startdate")
    LocalDateTime startDate;

    @Column(name = "enddate")
    LocalDateTime endDate;

    @Column(name = "status")
    String status;
    
    @ManyToMany(fetch = FetchType.LAZY) // Discount đang sở hữu quan hệ join nên không cần mappedBy
    @JsonManagedReference
    @JoinTable(name = "discounts_roomtypes", // chỉ định bảng trung gian tên discounts_roomtypes
                joinColumns = @JoinColumn(name = "discounts_id"), // Cột discounts_id trong bản trung gian liên kết với bảng Discount, nơi dòng lệnh đang ở
                inverseJoinColumns = @JoinColumn(name = "roomtype_id")) // Cột roomtype_id trong bảng trung gian liên kết với bảng RoomTypes, liên kết đến class khác.
    List<RoomType> roomTypes; 
}
