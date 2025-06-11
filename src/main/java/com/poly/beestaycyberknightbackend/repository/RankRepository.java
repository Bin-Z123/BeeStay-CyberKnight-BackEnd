package com.poly.beestaycyberknightbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.beestaycyberknightbackend.domain.Rank;

@Repository
public interface RankRepository extends JpaRepository<Rank,Integer>{
    boolean existsBynameRank(String nameRank);
    boolean existsById(int id);
}
