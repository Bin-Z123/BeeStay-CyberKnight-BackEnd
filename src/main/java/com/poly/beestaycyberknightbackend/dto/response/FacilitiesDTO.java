package com.poly.beestaycyberknightbackend.dto.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilitiesDTO {

    Long id;

    String facilityName;

    String description;

    BigDecimal price;

    String publicId;
}
