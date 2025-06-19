package com.poly.beestaycyberknightbackend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.poly.beestaycyberknightbackend.domain.Booking;
import com.poly.beestaycyberknightbackend.repository.BookingRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BookingService {
    BookingRepository bookingRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}