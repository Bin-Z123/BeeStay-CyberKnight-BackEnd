package com.poly.beestaycyberknightbackend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Rank")
public class Rank {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    int id;

    @Column(length = 30, nullable = true)
    String nameRank;

    @Column(nullable = true)
    int minPointRequired;

    @Column(nullable = true)
    int discount_percent;

    

}
