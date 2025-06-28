package com.poly.beestaycyberknightbackend.service;

import com.poly.beestaycyberknightbackend.domain.Booking;
import com.poly.beestaycyberknightbackend.domain.InfoGuest;
import com.poly.beestaycyberknightbackend.domain.Room;
import com.poly.beestaycyberknightbackend.domain.Stay;
import com.poly.beestaycyberknightbackend.dto.request.StayRequest;
import com.poly.beestaycyberknightbackend.dto.response.StayResponse;
import com.poly.beestaycyberknightbackend.mapper.InfoGuestMapper;
import com.poly.beestaycyberknightbackend.mapper.StayMapper;
import com.poly.beestaycyberknightbackend.repository.BookingRepository;
import com.poly.beestaycyberknightbackend.repository.InfoGuestRepository;
import com.poly.beestaycyberknightbackend.repository.RoomRepository;
import com.poly.beestaycyberknightbackend.repository.StayRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StayService {

    private final StayRepository stayRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final InfoGuestRepository infoGuestRepository;
    private final StayMapper stayMapper;
    private final InfoGuestMapper infoGuestMapper;

    public StayResponse handleCreateStay(StayRequest request) {
    // 1. Lấy Room theo ID
    Room room = roomRepository.findById(request.getRoomId())
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng với ID: " + request.getRoomId()));

    // 2. Lấy Booking theo ID
    Booking booking = bookingRepository.findById(request.getBookingId())
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy booking với ID: " + request.getBookingId()));

    // 3. Map StayRequest thành Stay entity
    Stay stay = stayMapper.toEntity(request);
    stay.setRoom(room);
    stay.setBooking(booking);

    // 4. Xử lý danh sách khách phụ nếu có
    if (request.getInfoGuests() != null && !request.getInfoGuests().isEmpty()) {
        List<InfoGuest> guests = request.getInfoGuests().stream()
                .map(g -> {
                    InfoGuest infoGuest = infoGuestMapper.toEntity(g);
                    infoGuest.setStay(stay);  // Gán stay trước khi save
                    return infoGuest;
                }).collect(Collectors.toList());

        // Gán danh sách infoGuests cho stay
        stay.setInfoGuests(guests);
    }

    // 5. Lưu stay (cùng cascade lưu infoGuests)
    Stay savedStay = stayRepository.save(stay);

    // 6. Trả về response
    return stayMapper.toStayResponse(savedStay);
}


    public List<StayResponse> fetchAllStays() {
        return stayRepository.findAll().stream()
                .map(stayMapper::toStayResponse)
                .collect(Collectors.toList());
    }

    public StayResponse fetchStayById(int id) {
        Stay stay = stayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stay không tồn tại với id: " + id));
        return stayMapper.toStayResponse(stay);
    }

    public void handleDeleteStay(int id) {
        stayRepository.deleteById(id);
    }

    // Nếu cần thêm cập nhật stay có thể viết thêm sau
}
