package com.poly.beestaycyberknightbackend.service;

import org.springframework.stereotype.Service;
import com.poly.beestaycyberknightbackend.repository.BookingRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PaymentService {
    BookingRepository bookingRepository;

    public Integer sumPaymentPAIDOfBooking(Long bookingId) {
        return bookingRepository.totalPaymentofBooking(bookingId);
    }

}
