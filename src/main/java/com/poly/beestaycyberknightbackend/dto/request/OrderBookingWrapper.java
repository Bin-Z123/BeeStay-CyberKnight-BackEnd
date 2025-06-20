package com.poly.beestaycyberknightbackend.dto.request;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderBookingWrapper {
     GuestBookingRequest guestBookingRequest;
     BookingRequest bookingRequest;
     List<BookingDetailRequest> bookingDetailRequest;
     List<BookingFacilityRequest> bookingFacilityRequest;
     List<StayRequest> stayRequest;

}