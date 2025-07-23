package com.poly.beestaycyberknightbackend.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.poly.beestaycyberknightbackend.domain.Payment;
import com.poly.beestaycyberknightbackend.dto.request.PaymentByCashRequest;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.service.PaymentByCashService;
import com.poly.beestaycyberknightbackend.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/admin/payment")
public class PaymentController {
    PaymentByCashService paymentByCastService;
    PaymentService paymentService;


    @PostMapping("/pay")
    public ApiResponse<Payment> createPaymentByCast(@RequestBody PaymentByCashRequest request) {
        return new ApiResponse<>(HttpStatus.SC_OK, null, paymentByCastService.createPaymentByCast(request));
    }

    @GetMapping("/calculatePaymentofBooking/{bookingId}")
    public ApiResponse<Integer> calculatePaymentofBooking(@PathVariable Long bookingId) {
        return new ApiResponse<>(HttpStatus.SC_OK, null, paymentService.sumPaymentPAIDOfBooking(bookingId));
    }
    
    
}
