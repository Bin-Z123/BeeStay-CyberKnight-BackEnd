package com.poly.beestaycyberknightbackend.domain;

import java.lang.invoke.StringConcatFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false, unique = true)
    String phone;

    @Column(length = 100,nullable = false, unique = true)
    String email;

    @Column(length = 255, nullable = false)
    String password;

    Boolean gender;

    LocalDate birthday;

    @Column(nullable = false)
    LocalDateTime joinDate = LocalDateTime.now();

    LocalDateTime updateDate;

    @Column(length = 100, nullable = false)
    String fullname;


    
    @Enumerated(EnumType.STRING)
    EBlacklist eBlacklist = EBlacklist.NONE;

    String cccd;

    int point = 0;
    
    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonBackReference
    private Role role;

    public enum EBlacklist {
        FIRST,
        SECOND,
        BLOCKED,
        NONE
    }
}
