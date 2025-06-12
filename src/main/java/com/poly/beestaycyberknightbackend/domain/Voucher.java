package com.poly.beestaycyberknightbackend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(length = 20, nullable = false)
    String code;

    @Column(nullable = false)
    Integer discountValue;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    VoucherStatus eStatus = VoucherStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY) // ẩn object khi không cần dùng
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rank_requirement_id")
    Rank rank;

    public enum VoucherStatus {
        ACTIVE,
        INACTIVE
    }

}
