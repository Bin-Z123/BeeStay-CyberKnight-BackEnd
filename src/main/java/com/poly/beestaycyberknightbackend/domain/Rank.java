package com.poly.beestaycyberknightbackend.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults( level = AccessLevel.PRIVATE)
@Entity
@Table(name = "Rank")
public class Rank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String nameRank;

    int minPointRequired;

    BigDecimal discountPercent;

    @OneToMany(mappedBy = "rank")
            @JsonManagedReference
    List<Voucher> vouchers;
}
