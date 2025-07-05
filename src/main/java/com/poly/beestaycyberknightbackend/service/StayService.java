package com.poly.beestaycyberknightbackend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.poly.beestaycyberknightbackend.domain.Booking;
import com.poly.beestaycyberknightbackend.domain.InfoGuest;
import com.poly.beestaycyberknightbackend.domain.Room;
import com.poly.beestaycyberknightbackend.domain.Stay;
import com.poly.beestaycyberknightbackend.dto.request.StayCreationRequest;
import com.poly.beestaycyberknightbackend.exception.AppException;
import com.poly.beestaycyberknightbackend.exception.ErrorCode;
import com.poly.beestaycyberknightbackend.mapper.InfoGuestMapper;
import com.poly.beestaycyberknightbackend.mapper.StayMapper;
import com.poly.beestaycyberknightbackend.repository.BookingRepository;
import com.poly.beestaycyberknightbackend.repository.RoomRepository;
import com.poly.beestaycyberknightbackend.repository.StayRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StayService {
    StayRepository stayRepository;
    StayMapper mapper;
    BookingRepository bookingRepository;
    RoomRepository roomRepository;
    InfoGuestMapper guestMapper;

    public Stay createStay(StayCreationRequest request){
        Room room = roomRepository.findById(request.getRoomId()).orElseThrow(()-> new AppException(ErrorCode.ROOM_NOT_EXISTED));
        Booking booking = bookingRepository.findById(request.getBookingId()).orElseThrow(()-> new AppException(ErrorCode.BOOKING_NOT_EXISTED));

        Stay stay = mapper.toStay(request);
        stay.setBooking(booking);
        stay.setRoom(room);
        
        List<InfoGuest> listguest = request.getInfoGuests().stream().map(
            guest -> {
                InfoGuest infoGuest = guestMapper.toEntity(guest);
                infoGuest.setStay(stay);
                return infoGuest;
            }
        ).collect(Collectors.toList());
        stay.setInfoGuests(listguest);

        

        booking.setBookingStatus("STAY");
        bookingRepository.save(booking);

        return stayRepository.save(stay);
    }
}
