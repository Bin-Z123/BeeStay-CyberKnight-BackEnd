package com.poly.beestaycyberknightbackend.mapper;

import org.mapstruct.Mapper;

import com.poly.beestaycyberknightbackend.domain.BookingDetail;
import com.poly.beestaycyberknightbackend.dto.request.BookingDetailRequest;

@Mapper(componentModel = "spring")
public interface BookingDetailMapper {
    BookingDetail toEntity(BookingDetailRequest request);
  
}
