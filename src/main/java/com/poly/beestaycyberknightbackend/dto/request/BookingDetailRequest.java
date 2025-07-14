package com.poly.beestaycyberknightbackend.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDetailRequest {

    Long roomTypeId;
    Integer quantity;
}
