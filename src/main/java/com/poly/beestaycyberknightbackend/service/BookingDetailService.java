package com.poly.beestaycyberknightbackend.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.poly.beestaycyberknightbackend.domain.Booking;
import com.poly.beestaycyberknightbackend.domain.BookingDetail;
import com.poly.beestaycyberknightbackend.domain.RoomType;
import com.poly.beestaycyberknightbackend.dto.request.BookingDetailUpdateRequest;
import com.poly.beestaycyberknightbackend.exception.AppException;
import com.poly.beestaycyberknightbackend.exception.ErrorCode;
import com.poly.beestaycyberknightbackend.mapper.BookingDetailMapper;
import com.poly.beestaycyberknightbackend.repository.BookingDetailRepository;
import com.poly.beestaycyberknightbackend.repository.BookingRepository;
import com.poly.beestaycyberknightbackend.repository.RoomTypeRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BookingDetailService {
    BookingDetailMapper bookingDetailMapper;
    BookingDetailRepository bookingDetailRepository;
    BookingRepository bookingRepository;
    RoomTypeRepository roomTypeRepository;

    public void updateBookingDetail(Long id, BookingDetailUpdateRequest bookingDetailUpdateRequest){
       BookingDetail bookingdetail = bookingDetailRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.BOOKINGDETAIL_NOT_EXISTED));
       Booking booking = bookingRepository.findById(bookingDetailUpdateRequest.getBookingId()).orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXISTED));
       RoomType roomType = roomTypeRepository.findById(bookingDetailUpdateRequest.getRoomTypeId()).orElseThrow(() -> new AppException(ErrorCode.ROOMTYPE_NOT_EXISTED));

       bookingdetail.setBooking(booking);
       bookingdetail.setRoomType(roomType);
       bookingdetail.setQuantity(bookingDetailUpdateRequest.getQuantity());

       bookingDetailRepository.save(bookingdetail);
    }
}
