package com.poly.beestaycyberknightbackend.domain.dto.response;

import com.poly.beestaycyberknightbackend.domain.Voucher;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults( level = AccessLevel.PRIVATE)
public class RankResponse {
    int id;
    String nameRank;
    int minPointRequired;
    BigDecimal discountPercent;
    List<Voucher> vouchers;
}
