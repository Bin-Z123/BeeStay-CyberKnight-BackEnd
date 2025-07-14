package com.poly.beestaycyberknightbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeResponseNotlistRoom {
        private long id;
    private String name;
    private int size;
    private int price;
    private int peopleAbout;
}
