package com.poly.beestaycyberknightbackend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.poly.beestaycyberknightbackend.domain.Booking;
import com.poly.beestaycyberknightbackend.domain.Payment;
import com.poly.beestaycyberknightbackend.dto.request.CreatePaymentLinkRequestBody;
import com.poly.beestaycyberknightbackend.dto.response.PaymentPayOSResponse;
import com.poly.beestaycyberknightbackend.exception.AppException;
import com.poly.beestaycyberknightbackend.exception.ErrorCode;
import com.poly.beestaycyberknightbackend.repository.BookingRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.PaymentLinkData;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PayOSService {
    PayOS payOS;
    BookingRepository bookingRepository;

    @Transactional
    public PaymentPayOSResponse createPaymentLink(CreatePaymentLinkRequestBody linkRequestBody) {
        try {
            Booking booking = bookingRepository.findById(linkRequestBody.getBookingId())
                    .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXISTED));
            List<Object[]> roomTypesList = bookingRepository.getRoomTypeBooking(linkRequestBody.getBookingId());
            List<Object[]> facilitiesList = bookingRepository.getFacilitiesBooking(linkRequestBody.getBookingId());

            StringBuilder billName = new StringBuilder();
            StringBuilder description = new StringBuilder();
            int totalAmount = booking.getTotalAmount();

            roomTypesList.stream().forEach(row -> {
                String roomtype = (String) row[0];
                Integer qty = Integer.parseInt((row[1]).toString());
                billName.append(roomtype).append(" x").append(qty).append(", ");
            });

            facilitiesList.stream().forEach(row1 -> {
                String namefacility = (String) row1[0];
                Integer qtyF = Integer.parseInt((row1[1]).toString());
                billName.append(namefacility).append(" x").append(qtyF).append(", ");
            });

            if (billName.length() > 2) {
                billName.setLength(billName.length() - 2);
            }

            String billNamelast = String.valueOf(billName);

            description.append("pay bill ")
                       .append(booking.getId());
                    
                    
            String descripstionlast = String.valueOf(description);

            //Tạo payment và lấy id payment làm ordercode
            Payment payment = new Payment();

            //Tạo item cho thanh toán
            ItemData itemData = ItemData.builder().name(billNamelast).price(totalAmount).quantity(1).build();

            PaymentData paymentData = PaymentData.builder().orderCode(booking.getId()).description(descripstionlast)
                    .amount(totalAmount).item(itemData).returnUrl(linkRequestBody.getReturnUrl())
                    .cancelUrl(linkRequestBody.getCancelUrl()).build();

            CheckoutResponseData data = payOS.createPaymentLink(paymentData);

            return new PaymentPayOSResponse<>(0, "success", data);
        } catch (Exception e) {
            e.printStackTrace();
            return new PaymentPayOSResponse<>(-1, "fail", e);
        }
    }


    public PaymentPayOSResponse getOrderById(long id){
        try {
            PaymentLinkData order = payOS.getPaymentLinkInformation(id);
            return new PaymentPayOSResponse<>(0, "sucess", order);
        } catch (Exception e) {
            e.printStackTrace();
            return new PaymentPayOSResponse(-1, e.getMessage(), null);
        }
    }

    public PaymentPayOSResponse cancelOrder(long id){
        try {
            PaymentLinkData order = payOS.cancelPaymentLink(id, null);
            return new PaymentPayOSResponse<>(0, "sucess", order);
        } catch (Exception e) {
            e.printStackTrace();
            return new PaymentPayOSResponse(-1, e.getMessage(), null);
        }
    }

    // public PaymentPayOSResponse confirmWebhook( Map<String, String> requestBody) {
       
    //     try {
    //         String str = payOS.confirmWebhook(requestBody.get("webhookUrl"));
            
    //         return new PaymentPayOSResponse<>(0, "ok", str);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return new PaymentPayOSResponse(-1, e.getMessage(), null);
    //     }
    // }
}
