package com.poly.beestaycyberknightbackend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "Voucher")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String code;
    BigDecimal discountValue;
    VoucherStatus eStatus;

    @ManyToOne
    @JoinColumn(name = "rank_requirement_id")
    @JsonBackReference
    Rank rank;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    User user;

    public enum VoucherStatus {
        ACTIVE, // Có thể sử dụng
        USED, // Đã được sử dụng
        LOCKED, // Chưa mở khóa voucher này
        CANCELED // Đã hủy bởi admin
    }
}
