package com.poly.beestaycyberknightbackend.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
    EBlacklist eBlacklist = EBlacklist.NORM;

    String cccd;

    int point = 0;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    @JsonBackReference
    Role role;


    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<TransactionLog> transantionLog;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rank_id")
    @JsonBackReference
    Rank rank;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<Booking> booking;

    public enum EBlacklist {
        NORM,
        WARN,
        BANN,
        NONE
    }
}
