package com.poly.beestaycyberknightbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.beestaycyberknightbackend.domain.RoomType;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long>{

    Optional<RoomType> findByNameAndSizeAndPriceAndPeopleAbout(String name, int size, int price, int peopleAbout);

    
}