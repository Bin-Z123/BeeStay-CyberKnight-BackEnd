package com.poly.beestaycyberknightbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.beestaycyberknightbackend.domain.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    List<Discount> findByRoomTypes_Id(Long roomTypeId); //Loại phòng đó đang áp dụng những discount.
    boolean existsById(Integer id);
}

