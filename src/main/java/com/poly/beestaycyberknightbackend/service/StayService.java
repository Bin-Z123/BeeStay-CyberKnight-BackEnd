package com.poly.beestaycyberknightbackend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public List<Stay> createMultipleStays(List<StayCreationRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        List<Stay> staysToSave = new ArrayList<>();

        // Lặp qua từng yêu cầu tạo Stay
        for (StayCreationRequest request : requests) {
            Room room = roomRepository.findById(request.getRoomId())
                    .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_EXISTED));
            Booking booking = bookingRepository.findById(request.getBookingId())
                    .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXISTED));

            // Tạo một đối tượng Stay mới cho mỗi request
            Stay stay = mapper.toStay(request);
            stay.setBooking(booking);
            stay.setRoom(room);

            // Xử lý danh sách khách cho từng Stay
            if (request.getInfoGuests() != null && !request.getInfoGuests().isEmpty()) {
                List<InfoGuest> listguest = request.getInfoGuests().stream().map(
                        guestRequest -> {
                            InfoGuest infoGuest = guestMapper.toEntity(guestRequest);
                            infoGuest.setStay(stay); // Gán vào đối tượng stay hiện tại
                            return infoGuest;
                        }).collect(Collectors.toList());
                stay.setInfoGuests(listguest);
            }

            // Cập nhật trạng thái của booking (chỉ cần làm một lần nếu tất cả stay thuộc
            // cùng booking)
            // Nếu các stay có thể thuộc các booking khác nhau, logic này cần được xem xét
            // lại.
            if (!booking.getBookingStatus().equals("STAY")) {
                booking.setBookingStatus("STAY");
                bookingRepository.save(booking);
            }

            staysToSave.add(stay);
        }

        // Lưu tất cả các Stay mới vào DB trong một lần duy nhất để tối ưu hiệu suất
        return stayRepository.saveAll(staysToSave);
    }

}
