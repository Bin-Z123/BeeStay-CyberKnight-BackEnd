package com.poly.beestaycyberknightbackend.dto.response;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableRoomDTO {
    String nameRoomType;
    long id;
    String roomNumber;
    int roomTypeId;
    String roomStatus;
    int floor;
    
}
