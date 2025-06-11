package com.poly.beestaycyberknightbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomTypeRequest {
    private String name;
    private int size;
    private int price;
    private int peopleAbout;
}
