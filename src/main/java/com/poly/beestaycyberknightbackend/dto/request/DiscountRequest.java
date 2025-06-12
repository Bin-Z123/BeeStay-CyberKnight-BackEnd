package com.poly.beestaycyberknightbackend.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiscountRequest {
    String title;
    String description;
    String discountCode;
    String discountType;
    String discountValue;
    LocalDateTime startDate;
    LocalDateTime endDate;
    String status;
    List<Long> roomTypeIds; // Chỉ lưu ID thay vì toàn bộ entity

}
