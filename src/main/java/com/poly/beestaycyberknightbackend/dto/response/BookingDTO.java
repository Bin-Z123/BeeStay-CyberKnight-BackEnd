package com.poly.beestaycyberknightbackend.dto.response;

import java.time.LocalDateTime;
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
public class BookingDTO {

    long id;

    LocalDateTime checkInDate;

    LocalDateTime checkOutDate;

    Integer totalAmount;

    Boolean isDeposit;

    String bookingStatus;

    LocalDateTime bookingDate ;

    Integer numGuest;

    UserResponse user;

    GuestBookingDTO guestBooking;

    List<BookingDetailDTO> bookingDetails;

    List<BookingFacilitiesDTO> bookingFacilities;

    List<StayDTO> stay;


}
