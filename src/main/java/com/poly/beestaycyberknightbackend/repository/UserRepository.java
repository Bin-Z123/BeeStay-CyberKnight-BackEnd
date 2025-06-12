package com.poly.beestaycyberknightbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.beestaycyberknightbackend.domain.User;

public interface UserRepository  extends JpaRepository<User, Long> {

    User findByEmail(String email);
    

}