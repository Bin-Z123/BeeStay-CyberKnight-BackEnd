package com.poly.beestaycyberknightbackend.domain.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomRequest {
    private String roomNumber;    
    private String roomStatus;    
    private int floor;           

    private long roomTypeId;    
    private List<RoomImageRequest> roomImages;

}
