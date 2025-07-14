package com.poly.beestaycyberknightbackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.poly.beestaycyberknightbackend.domain.Booking;
import com.poly.beestaycyberknightbackend.domain.Payment;
import com.poly.beestaycyberknightbackend.dto.request.CreatePaymentLinkRequestBody;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.dto.response.PaymentPayOSResponse;
import com.poly.beestaycyberknightbackend.exception.AppException;
import com.poly.beestaycyberknightbackend.exception.ErrorCode;
import com.poly.beestaycyberknightbackend.repository.BookingRepository;
import com.poly.beestaycyberknightbackend.repository.PaymentRepository;
import com.poly.beestaycyberknightbackend.service.BookingService;
import com.poly.beestaycyberknightbackend.service.PayOSService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.payos.PayOS;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

import java.time.LocalDateTime;
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
    PayOS payOS;
    BookingRepository bookingRepository;
    PaymentRepository paymentRepository;
    BookingService bookingService;

    @PostMapping("/create")
    public ApiResponse<PaymentPayOSResponse> createLinkPayment(@RequestBody CreatePaymentLinkRequestBody requestBody) {
        return new ApiResponse<>(200, null, osService.createPaymentLink(requestBody));
    }

    @GetMapping("/{Id}")
    public ApiResponse<PaymentPayOSResponse> getOrderBookingById(@PathVariable long Id) {
        return new ApiResponse<>(200, null, osService.getOrderById(Id));
    }

    @PutMapping("/{Id}")
    public ApiResponse<PaymentPayOSResponse> cancelPay(@PathVariable long Id) {
        return new ApiResponse<>(200, null, osService.cancelOrder(Id));
    }

    // @PostMapping("/confirm-webhook")
    // public ApiResponse<PaymentPayOSResponse> confirmWebhook(@RequestBody
    // Map<String, String> requestBody) {
    // return new ApiResponse<>(200, null, osService.confirmWebhook(requestBody));
    // }
    

    @PostMapping("/payos_transfer_handler")
    public ObjectNode payosTransferHandler(@RequestBody ObjectNode body)
            throws JsonProcessingException, IllegalArgumentException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        Webhook webhookBody = objectMapper.treeToValue(body, Webhook.class);

        try {
            WebhookData data = payOS.verifyPaymentWebhookData(webhookBody);
            boolean success = body.get("success").asBoolean();
            if (success) {
                Booking booking = bookingRepository.findById(data.getOrderCode()).orElseThrow(()-> new AppException(ErrorCode.BOOKING_NOT_EXISTED));
                Payment payment = new Payment();
                payment.setAmount(data.getAmount());
                payment.setBooking(booking);
                payment.setPaymentCode(data.getCode());
                payment.setPaymentDate(LocalDateTime.now());
                payment.setPaymentMethod("BANK_TRANSFER");
                payment.setPaymentStatus("SUCCESS");
                payment.setPaymentType("NONE");
                paymentRepository.save(payment);
                paymentRepository.flush();

                bookingService.checkTotalPaymentofBooking(booking.getId());
            }
            response.put("error", 0);
            response.put("message", "Webhook delivered");
            response.set("data", null);
            System.out.println(data);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }

    }
    
    @PostMapping(path = "/confirm-webhook")
    public ObjectNode confirmWebhook(@RequestBody Map<String, String> requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            String str = payOS.confirmWebhook(requestBody.get("webhookUrl"));
            response.set("data", objectMapper.valueToTree(str));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

}
