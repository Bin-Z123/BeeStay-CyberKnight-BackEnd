package com.poly.beestaycyberknightbackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.beestaycyberknightbackend.dto.request.CreatePaymentLinkRequestBody;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.dto.response.PaymentPayOSResponse;
import com.poly.beestaycyberknightbackend.service.PayOSService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/orderPayOS")
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
@RequiredArgsConstructor
public class PaymentPayOSController {
    PayOSService osService;

    @PostMapping("/create")
    public ApiResponse<PaymentPayOSResponse> createLinkPayment(@RequestBody CreatePaymentLinkRequestBody requestBody) {
        return new ApiResponse<>(200, null, osService.createPaymentLink(requestBody));
    }
    
    @GetMapping("/{bookingId}")
    public ApiResponse<PaymentPayOSResponse> getOrderBookingById(@PathVariable long Id) {
        return new ApiResponse<>(200, null, osService.getOrderById(Id));
    }

    @PutMapping("/{bookingId}")
    public ApiResponse<PaymentPayOSResponse> cancelPay(@PathVariable long Id) {
        return new ApiResponse<>(200, null, osService.cancelOrder(Id));
    }

    // @PostMapping("/confirm-webhook")
    // public ApiResponse<PaymentPayOSResponse> confirmWebhook(@RequestBody Map<String, String> requestBody) {
    //     return new ApiResponse<>(200, null, osService.confirmWebhook(requestBody));
    // }
    

}
