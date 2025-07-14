package com.poly.beestaycyberknightbackend.service;

import org.springframework.stereotype.Service;

import com.poly.beestaycyberknightbackend.domain.Booking;
import com.poly.beestaycyberknightbackend.domain.Payment;
import com.poly.beestaycyberknightbackend.dto.request.PaymentByCastRequest;
import com.poly.beestaycyberknightbackend.exception.AppException;
import com.poly.beestaycyberknightbackend.exception.ErrorCode;
import com.poly.beestaycyberknightbackend.mapper.PaymentMapper;
import com.poly.beestaycyberknightbackend.repository.BookingRepository;
import com.poly.beestaycyberknightbackend.repository.PaymentRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PaymentByCastService {
    PaymentRepository paymentRepository;
    PaymentMapper paymentMapper;
    BookingRepository bookingRepository;
    BookingService bookingService;

    public Payment createPaymentByCast(PaymentByCastRequest request){
        Booking booking = bookingRepository.findById(request.getBookingId()).orElseThrow(()-> new AppException(ErrorCode.BOOKING_NOT_EXISTED));

        Payment payment = paymentMapper.toPayment(request);
        payment.setBooking(booking);
        payment.setPaymentMethod("CAST");
        payment.setPaymentStatus("PAIED");
        payment.setPaymentType("FULL");

        paymentRepository.save(payment);
        paymentRepository.flush();
        
        bookingService.checkTotalPaymentofBooking(booking.getId());
        
        return payment;
    }
}
