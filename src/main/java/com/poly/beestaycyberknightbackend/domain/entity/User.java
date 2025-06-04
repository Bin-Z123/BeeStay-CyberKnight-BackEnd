package com.poly.beestaycyberknightbackend.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    int phone;

    @Column(length = 100,nullable = false, unique = true)
    String email;

    @Column(length = 255, nullable = false)
    String password;

    Boolean gender;

    LocalDate birthdate;

    @Column(nullable = false)
    LocalDateTime joinDate = LocalDateTime.now();

    LocalDateTime updateDate;

    @Column(length = 100, nullable = false)
    String fullname;

    public enum EBlacklist {
        FIRST,
        SECOND,
        BLOCKED,
        NONE
    }
    
    @Enumerated(EnumType.STRING)
    EBlacklist eBlacklist = EBlacklist.NONE;

    int cccd;

    int point;
    
    @OneToMany(mappedBy = "user")
    List<UserRole> roles;

}
