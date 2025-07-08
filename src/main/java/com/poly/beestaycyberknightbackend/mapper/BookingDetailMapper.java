package com.poly.beestaycyberknightbackend.mapper;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.poly.beestaycyberknightbackend.domain.BookingDetail;
import com.poly.beestaycyberknightbackend.dto.request.BookingDetailRequest;
import com.poly.beestaycyberknightbackend.dto.request.BookingDetailUpdateRequest;
import com.poly.beestaycyberknightbackend.dto.response.BookingDetailDTO;

@Mapper(componentModel = "spring")
public interface BookingDetailMapper {
    BookingDetail toEntity(BookingDetailRequest request);
    
    void toUpdateEntity(@MappingTarget Optional<BookingDetail> bookingdetail,BookingDetailUpdateRequest bookingDetailUpdateRequest);

    BookingDetailDTO toResponse(BookingDetail bookingDetail);
}
