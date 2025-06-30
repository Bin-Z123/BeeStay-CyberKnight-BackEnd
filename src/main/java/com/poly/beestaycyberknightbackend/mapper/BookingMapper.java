package com.poly.beestaycyberknightbackend.mapper;

import org.mapstruct.Mapper;

import com.poly.beestaycyberknightbackend.domain.Booking;
import com.poly.beestaycyberknightbackend.dto.request.BookingRequest;
import com.poly.beestaycyberknightbackend.dto.response.BookingResponse;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking toBooking(BookingRequest bookingRequest);
    BookingResponse toResponse(Booking booking);
}
