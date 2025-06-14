package com.poly.beestaycyberknightbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.beestaycyberknightbackend.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

}
