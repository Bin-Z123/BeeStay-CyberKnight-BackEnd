package com.poly.beestaycyberknightbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomImageRequest {
    private String url;    
    private String altext;  
    private Boolean isThum; 
}
