package com.poly.beestaycyberknightbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;
import com.poly.beestaycyberknightbackend.domain.TransactionLog;
import com.poly.beestaycyberknightbackend.domain.User;

import java.util.List;


@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
    List<TransactionLog> findByUser(User user);

    @Query("SELECT t FROM TransactionLog t JOIN FETCH t.user")
    List<TransactionLog> findAllWithUser();
    
}
