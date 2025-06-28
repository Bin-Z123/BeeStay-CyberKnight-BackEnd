package com.poly.beestaycyberknightbackend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResponse {
    Long id;
    LocalDateTime checkInDate;
    LocalDateTime checkOutDate;
    Integer totalAmount;
    Boolean isDeposit;
    String bookingStatus;
    LocalDateTime bookingDate;
    Integer numGuest;
    Long userId;         
    String userFullName; 
    Integer numberOfNights;
    List<BookingDetailResponse> bookingDetails;
}
