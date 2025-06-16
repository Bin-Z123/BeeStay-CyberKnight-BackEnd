package com.poly.beestaycyberknightbackend.dto.response;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE)
public class RankResponse {
    int id;
    String nameRank;
    int minPointRequired;
    int discount_percent;
}
