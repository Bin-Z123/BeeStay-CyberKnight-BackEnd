package com.poly.beestaycyberknightbackend.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.poly.beestaycyberknightbackend.domain.InfoGuest;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StayDTO {

    int id;

    Long roomId;

    LocalDateTime actualCheckIn;

    LocalDateTime actualCheckOut;

    String stayStatus;

    LocalDateTime createdAt = LocalDateTime.now();

    String note;

    List<InfoGuest> infoGuests;

}
