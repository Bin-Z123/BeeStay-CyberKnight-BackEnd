package com.poly.beestaycyberknightbackend.mapper;

import org.mapstruct.Mapper;

import com.poly.beestaycyberknightbackend.domain.GuestBooking;
import com.poly.beestaycyberknightbackend.dto.request.GuestBookingRequest;

@Mapper(componentModel = "spring")
public interface GuestBookingMapper {
    
    GuestBooking toGuestBooking(GuestBookingRequest request);

}
