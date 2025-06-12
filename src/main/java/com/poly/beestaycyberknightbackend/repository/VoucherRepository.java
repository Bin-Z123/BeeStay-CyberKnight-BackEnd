package com.poly.beestaycyberknightbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.beestaycyberknightbackend.domain.Voucher;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher,Integer>{
    boolean existsByCode (String code);
}
