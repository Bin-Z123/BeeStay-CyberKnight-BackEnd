package com.poly.beestaycyberknightbackend.service;


import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.poly.beestaycyberknightbackend.domain.Booking;
import com.poly.beestaycyberknightbackend.domain.Payment;
import com.poly.beestaycyberknightbackend.dto.request.CreatePaymentLinkRequestBody;
import com.poly.beestaycyberknightbackend.dto.response.PaymentPayOSResponse;
import com.poly.beestaycyberknightbackend.exception.AppException;
import com.poly.beestaycyberknightbackend.exception.ErrorCode;
import com.poly.beestaycyberknightbackend.repository.BookingRepository;
import com.poly.beestaycyberknightbackend.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.PaymentLinkData;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PayOSService {
    PayOS payOS;
    BookingRepository bookingRepository;
    PaymentRepository paymentRepository;
    BookingService bookingService;

    @Transactional
    public PaymentPayOSResponse createPaymentLink(CreatePaymentLinkRequestBody linkRequestBody) {
        try {
            Booking booking = bookingRepository.findById(linkRequestBody.getBookingId())
                    .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXISTED));

            Payment payment = new Payment();
            payment.setAmount(booking.getTotalAmount());
            payment.setBooking(booking);
            payment.setPaymentMethod("BANK");
            payment.setPaymentDate(LocalDateTime.now());
            payment.setPaymentStatus("PENDING");
            payment.setPaymentType("BOOKING");
            paymentRepository.save(payment);

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

            description.append("pay bill ").append(booking.getId());

            String descripstionlast = String.valueOf(description);

            // Tạo item cho thanh toán
            ItemData itemData = ItemData.builder().name(billNamelast).price(totalAmount).quantity(1).build();

            PaymentData paymentData = PaymentData.builder().orderCode(payment.getId()).description(descripstionlast)
                    .amount(totalAmount).item(itemData).returnUrl(linkRequestBody.getReturnUrl())
                    .cancelUrl(linkRequestBody.getCancelUrl()).build();

            CheckoutResponseData data = payOS.createPaymentLink(paymentData);

            return new PaymentPayOSResponse<>(0, "success", data);
        } catch (Exception e) {
            e.printStackTrace();
            return new PaymentPayOSResponse<>(-1, "fail", e);
        }
    }

    public PaymentPayOSResponse getOrderById(long id) {
        try {
            PaymentLinkData order = payOS.getPaymentLinkInformation(id);
            return new PaymentPayOSResponse<>(0, "sucess", order);
        } catch (Exception e) {
            e.printStackTrace();
            return new PaymentPayOSResponse(-1, e.getMessage(), null);
        }
    }


    @Transactional
    public PaymentPayOSResponse cancelOrderByPaymentId(long paymentId) {
        try {
            PaymentLinkData order = payOS.cancelPaymentLink(paymentId, null);

            // Cập nhật trạng thái của Payment
            Payment paymentToUpdate = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_EXISTED));
            paymentToUpdate.setPaymentStatus("CANCELLED");
            paymentRepository.save(paymentToUpdate);

            return new PaymentPayOSResponse<>(0, "sucess", order);
        } catch (Exception e) {
            e.printStackTrace();
            return new PaymentPayOSResponse(-1, e.getMessage(), null);
        }
    }

    @Transactional
    public PaymentPayOSResponse cancelPendingPaymentByBookingId(long bookingId) {
        try {
            // Tìm payment gần nhất với trạng thái pending
            Payment paymentToCancel = paymentRepository
                    .findFirstByBookingIdAndPaymentStatusOrderByPaymentDateDesc(bookingId, "PENDING")
                    .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_EXISTED));

            // Gọi phương thức hủy theo paymentId
            return cancelOrderByPaymentId(paymentToCancel.getId());

        } catch (Exception e) {
            e.printStackTrace();
            return new PaymentPayOSResponse(-1, e.getMessage(), null);
        }
    }

    @Transactional
    public void handleWebhookData(WebhookData data, ObjectNode body){
        // lấy paymentId từ webhook 
        Long paymentId = data.getOrderCode();

        //Tìm payment trong db
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(()-> new AppException(ErrorCode.PAYMENT_NOT_EXISTED));
        
        //Kiểm tra để tránh bị spam
        if ("PAID".equals(payment.getPaymentStatus())){
            System.out.println("Webhook đã được xử lý cho payment Id: " + paymentId);
            return;
        }

        //Xử lý webhook
        if (body.get("success").asBoolean(true)) { 
            payment.setPaymentStatus("PAID");
            payment.setPaymentCode(data.getCode());
            payment.setRawResponse(data.toString());
            paymentRepository.save(payment);

            //lấy booking ra để update trạng thái
            Booking booking = payment.getBooking();
            if(booking != null){
                bookingService.checkTotalPaymentofBooking(booking.getId());
            }
        } else {
            // xử lý giao dịch thất bại
            payment.setPaymentStatus("FAILED");
            payment.setRawResponse(data.toString());
            payment.setPaymentCode(data.getCode());
            paymentRepository.save(payment);
        }

        
    }

}
