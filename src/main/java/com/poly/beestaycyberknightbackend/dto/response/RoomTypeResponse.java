package com.poly.beestaycyberknightbackend.dto.response;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomTypeResponse {
    private long id;
    private String name;
    private int size;
    private int price;
    private int peopleAbout;
}
