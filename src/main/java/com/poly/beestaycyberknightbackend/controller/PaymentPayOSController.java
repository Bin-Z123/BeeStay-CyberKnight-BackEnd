package com.poly.beestaycyberknightbackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.poly.beestaycyberknightbackend.dto.request.CreatePaymentLinkRequestBody;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.dto.response.PaymentPayOSResponse;
import com.poly.beestaycyberknightbackend.service.PayOSService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.payos.PayOS;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/orderPayOS")
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
@RequiredArgsConstructor
public class PaymentPayOSController {
    PayOSService osService;
    PayOS payOS;

    @PostMapping("/create")
    public ApiResponse<PaymentPayOSResponse> createLinkPayment(@RequestBody CreatePaymentLinkRequestBody requestBody) {
        return new ApiResponse<>(200, null, osService.createPaymentLink(requestBody));
    }

    @GetMapping("/{orderCode}")
    public ApiResponse<PaymentPayOSResponse> getOrderBookingById(@PathVariable Long orderCode) {
        return new ApiResponse<>(200, null, osService.getOrderById(orderCode));
    }

    @PutMapping("/cancel-by-booking/{bookingId}")
    public ApiResponse<PaymentPayOSResponse> cancelPay(@PathVariable long bookingId) {
        return new ApiResponse<>(200, null, osService.cancelPendingPaymentByBookingId(bookingId));
    }

<<<<<<< Updated upstream
    // @PostMapping("/confirm-webhook")
    // public ApiResponse<PaymentPayOSResponse> confirmWebhook(@RequestBody
    // Map<String, String> requestBody) {
    // return new ApiResponse<>(200, null, osService.confirmWebhook(requestBody));
    // }
    

=======
>>>>>>> Stashed changes
    @PostMapping("/payos_transfer_handler")
    public ObjectNode payosTransferHandler(@RequestBody ObjectNode body)
            throws JsonProcessingException, IllegalArgumentException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        try {
            Webhook webhookBody = objectMapper.treeToValue(body, Webhook.class);
            WebhookData data = payOS.verifyPaymentWebhookData(webhookBody); 

            osService.handleWebhookData(data, body); 

            response.put("error", 0);
            response.put("message", "Webhook delivered");
            response.set("data", null);
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
