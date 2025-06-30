package com.poly.beestaycyberknightbackend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StayResponse {
    int id;
    LocalDateTime actualCheckIn;
    LocalDateTime actualCheckOut;
    String stayStatus;
    LocalDateTime createdAt;
    String note;

    RoomResponse room;
    BookingResponse booking;
    List<InfoGuestResponse> infoGuests;
}
