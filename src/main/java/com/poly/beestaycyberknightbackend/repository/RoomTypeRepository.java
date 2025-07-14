package com.poly.beestaycyberknightbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.beestaycyberknightbackend.domain.RoomType;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long>{

    Optional<RoomType> findByNameAndSizeAndPriceAndPeopleAbout(String name, int size, int price, int peopleAbout);

    
}