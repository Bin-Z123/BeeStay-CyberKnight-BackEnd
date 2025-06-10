package com.poly.beestaycyberknightbackend.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomImageResponse {
    private long id;
    private String url;
    private String altext;
    private Boolean isThum;
}
