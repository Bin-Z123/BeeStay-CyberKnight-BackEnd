package com.poly.beestaycyberknightbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomUpdateRequest {
    private long id;
    private String roomNumber;
    private String roomStatus;
    private int floor;

    private long roomTypeId;
    private List<RoomImageRequest> roomImages;
    private List<String> deletedRoomImageIds;
}
