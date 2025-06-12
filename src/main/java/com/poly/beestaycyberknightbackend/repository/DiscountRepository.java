package com.poly.beestaycyberknightbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.beestaycyberknightbackend.domain.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    List<Discount> findByRoomTypes_Id(int roomTypeId); //Những discount đang được áp dụng cho loại phòng.
    boolean existsById(Integer id);
}

