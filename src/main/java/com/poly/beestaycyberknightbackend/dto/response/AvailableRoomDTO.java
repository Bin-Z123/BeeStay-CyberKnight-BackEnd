package com.poly.beestaycyberknightbackend.dto.response;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableRoomDTO {
    long id;
    String roomNumber;
    int roomTypeId;
    String roomStatus;
    int floor;
    
}
