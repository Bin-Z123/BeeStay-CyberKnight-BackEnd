package com.poly.beestaycyberknightbackend.domain;

import lombok.experimental.FieldDefaults;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE )
@NoArgsConstructor
@AllArgsConstructor
public class Rank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int Id;

    @Column(length = 10, nullable = false)
    String nameRank;
    
    @Column(nullable = false)
    int minPointRequired;

    @Column(nullable = false)
    int discountPercent;

    @OneToMany
    @JsonManagedReference
    Voucher voucher;
    
    @OneToMany
    @JsonManagedReference
    User user;
}
