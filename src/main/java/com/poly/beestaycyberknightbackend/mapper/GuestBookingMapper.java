package com.poly.beestaycyberknightbackend.mapper;

import org.mapstruct.Mapper;

import com.poly.beestaycyberknightbackend.domain.GuestBooking;
import com.poly.beestaycyberknightbackend.dto.request.GuestBookingRequest;
import com.poly.beestaycyberknightbackend.dto.response.GuestBookingDTO;

@Mapper(componentModel = "spring")
public interface GuestBookingMapper {

    GuestBooking toGuestBooking(GuestBookingRequest request);
    GuestBookingDTO toResp (GuestBooking guestBooking);
}
