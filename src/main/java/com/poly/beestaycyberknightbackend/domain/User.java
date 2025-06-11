package com.poly.beestaycyberknightbackend.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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
    Role role;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    List<Voucher> vouchers;


    public enum EBlacklist {
        FIRST,
        SECOND,
        BLOCKED,
        NONE
    }
}
