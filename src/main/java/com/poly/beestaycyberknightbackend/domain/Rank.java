package com.poly.beestaycyberknightbackend.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy = "rank", fetch = FetchType.LAZY)
    @JsonIgnore
    List<User> User;

    

}
