package com.poly.beestaycyberknightbackend.domain.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransantionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    Long id;

    @Column(length = 100, nullable = false)
    String actionType;

    @Column(nullable = false)
    LocalDateTime logAt = LocalDateTime.now();

    @Column(length = 100)
    String ip;

    @Column(nullable = false)
    int amount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

}
