package com.poly.beestaycyberknightbackend.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudinary.Api;
import com.poly.beestaycyberknightbackend.domain.Payment;
import com.poly.beestaycyberknightbackend.dto.request.PaymentByCastRequest;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.service.PaymentByCastService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/admin/payment")
public class PaymentController {
    PaymentByCastService paymentByCastService;


    @PostMapping("/pay")
    public ApiResponse<Payment> createPaymentByCast(@RequestBody PaymentByCastRequest request) {
        return new ApiResponse<>(HttpStatus.SC_OK, null, paymentByCastService.createPaymentByCast(request));
    }
    
}
