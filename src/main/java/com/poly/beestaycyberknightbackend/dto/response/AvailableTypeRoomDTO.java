package com.poly.beestaycyberknightbackend.dto.response;

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
public class AvailableTypeRoomDTO {
    Long roomTypeId;
    int totalRooms;
    int fixRooms;
    int usedRooms;
    int availableRooms;
    List<AvailableRoomDTO> AvailableRoomDTO;
}
