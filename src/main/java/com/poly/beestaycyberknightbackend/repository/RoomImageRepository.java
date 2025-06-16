package com.poly.beestaycyberknightbackend.repository;

import com.poly.beestaycyberknightbackend.domain.RoomImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomImageRepository extends JpaRepository<RoomImage, Long> {
  void deleteByUrl(String url);
}