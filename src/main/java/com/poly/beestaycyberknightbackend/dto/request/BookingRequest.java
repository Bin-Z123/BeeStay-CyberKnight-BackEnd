package com.poly.beestaycyberknightbackend.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRequest {

    LocalDateTime checkInDate;
    LocalDateTime checkOutDate;
    Integer totalAmount;
    Boolean isDeposit;
    String bookingStatus;
    LocalDateTime bookingDate;
    Integer numGuest;
    long userId;

    // Danh sách phòng được đặt
    List<BookingDetailRequest> bookingDetails;
}
