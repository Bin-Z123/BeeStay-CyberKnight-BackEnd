package com.poly.beestaycyberknightbackend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDetailResponse {
    Long roomTypeId;
    String roomTypeName;
    Integer quantity;
    Integer roomTypeSize;
    Integer roomTypePrice;
    Integer peopleAbout;
}
