package com.poly.beestaycyberknightbackend.domain;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "roles")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    Long id;

    @Column(length = 100, nullable = false)
    String reportType;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    LocalDateTime reportAt;


    @Enumerated(EnumType.STRING)
    ReportStatus status;



    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    public enum ReportStatus {
        PENDING,
        RESOLVED,
        REJECTED
    }
}
