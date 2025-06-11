package com.poly.beestaycyberknightbackend.domain.dto.response;

import com.poly.beestaycyberknightbackend.domain.Voucher;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults( level = AccessLevel.PRIVATE)
public class VoucherResponse {
    int id;
    String code;
    BigDecimal discountValue;
    Voucher.VoucherStatus eStatus;
}
