package com.poly.beestaycyberknightbackend.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import com.poly.beestaycyberknightbackend.domain.InfoGuest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StayCreationRequest {
    Long roomId;
    Long bookingId;
    LocalDateTime actualCheckIn = LocalDateTime.now();
    LocalDateTime actualCheckOut = null;
    String stayStatus = "NOW";
    LocalDateTime createdAt = LocalDateTime.now();
    String note;
    List<InfoGuestRequest> infoGuests;
}
