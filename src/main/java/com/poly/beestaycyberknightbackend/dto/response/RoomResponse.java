package com.poly.beestaycyberknightbackend.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomResponse {
    private long id;
    private String roomNumber;    
    private String roomStatus;    
    private int floor;     
    private RoomTypeResponseNotlistRoom roomType;
    private List<RoomImageResponse> roomImages;
    private List<StayDTO> stay;
    private List<BookingDTO> booking;   
}
