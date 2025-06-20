package com.poly.beestaycyberknightbackend.dto.request;

import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRequest {

    LocalDateTime checkInDate;

    LocalDateTime checkOutDate;

    Boolean isDeposit;

    String bookingStatus;

    Integer numGuest;

    Long userId;

}
