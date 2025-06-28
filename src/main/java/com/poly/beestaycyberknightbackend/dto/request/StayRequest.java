package com.poly.beestaycyberknightbackend.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StayRequest {
    long roomId;
    long bookingId;
    String roomNumber;
    LocalDateTime actualCheckIn;
    LocalDateTime actualCheckOut;
    String stayStatus;
    String note;
    // Chỉ có khách phụ (infoGuests) nếu cần, có thể null hoặc empty
    List<InfoGuestRequest> infoGuests;
}

