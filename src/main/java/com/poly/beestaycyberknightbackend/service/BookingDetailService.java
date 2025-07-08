package com.poly.beestaycyberknightbackend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.poly.beestaycyberknightbackend.domain.Booking;
import com.poly.beestaycyberknightbackend.domain.BookingDetail;
import com.poly.beestaycyberknightbackend.domain.RoomType;
import com.poly.beestaycyberknightbackend.dto.request.BookingDetailRequest;
import com.poly.beestaycyberknightbackend.dto.request.BookingDetailUpdateRequest;
import com.poly.beestaycyberknightbackend.dto.response.BookingDetailDTO;
import com.poly.beestaycyberknightbackend.dto.response.RoomTypeDTO;
import com.poly.beestaycyberknightbackend.exception.AppException;
import com.poly.beestaycyberknightbackend.exception.ErrorCode;
import com.poly.beestaycyberknightbackend.mapper.BookingDetailMapper;
import com.poly.beestaycyberknightbackend.mapper.RoomTypeMapper;
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
    BookingDetailRepository bookingDetailRepository;
    BookingRepository bookingRepository;
    RoomTypeRepository roomTypeRepository;
    BookingDetailMapper bookingDetailMapper;


    public List<BookingDetailDTO> updateBookingDetail(Long bookingId,
            List<BookingDetailUpdateRequest> bookingDetailUpdateRequest) {
        List<BookingDetailUpdateRequest> listUpdate = new ArrayList<>();
        List<BookingDetailUpdateRequest> listCreate = new ArrayList<>();
        List<BookingDetail> listResult = new ArrayList<>();

        bookingDetailUpdateRequest.forEach(list -> {
            if (list.getId() != null) {
                listUpdate.add(list);
            } else if (list.getId() == null) {
                listCreate.add(list);
            }
        });

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXISTED));

        if (!listUpdate.isEmpty()) {
            listUpdate.forEach(list -> {

                RoomType roomType = roomTypeRepository.findById(list.getRoomTypeId())
                        .orElseThrow(() -> new AppException(ErrorCode.ROOMTYPE_NOT_EXISTED));
                BookingDetail bookingDetail = bookingDetailRepository.findById(list.getId())
                        .orElseThrow(() -> new AppException(ErrorCode.BOOKINGDETAIL_NOT_EXISTED));
                bookingDetail.setBooking(booking);
                bookingDetail.setRoomType(roomType);
                bookingDetail.setQuantity(list.getQuantity());

                bookingDetailRepository.save(bookingDetail);
                bookingDetailRepository.flush();

                listResult.add(bookingDetail);
            });

        }

        if (!listCreate.isEmpty()) {
            listCreate.forEach(list -> {

                RoomType roomType = roomTypeRepository.findById(list.getRoomTypeId())
                        .orElseThrow(() -> new AppException(ErrorCode.ROOMTYPE_NOT_EXISTED));
                BookingDetail bookingDetail = new BookingDetail();
                bookingDetail.setBooking(booking);
                bookingDetail.setRoomType(roomType);
                bookingDetail.setQuantity(list.getQuantity());

                bookingDetailRepository.save(bookingDetail);
                bookingDetailRepository.flush();

                listResult.add(bookingDetail);
            });

        }

        List<BookingDetailDTO> lisDtos = listResult.stream().map(
            list -> {
                BookingDetailDTO bDto = bookingDetailMapper.toResponse(list);
                return bDto;
            }
        ).collect(Collectors.toList());

        return lisDtos;
    }
}
