package com.poly.beestaycyberknightbackend.service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.poly.beestaycyberknightbackend.domain.Booking;
import com.poly.beestaycyberknightbackend.dto.request.CreatePaymentLinkRequestBody;
import com.poly.beestaycyberknightbackend.dto.response.PaymentPayOSResponse;
import com.poly.beestaycyberknightbackend.exception.AppException;
import com.poly.beestaycyberknightbackend.exception.ErrorCode;
import com.poly.beestaycyberknightbackend.repository.BookingRepository;

import jakarta.mail.FetchProfile.Item;
import jakarta.persistence.criteria.CriteriaBuilder.In;
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

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime fromDate = booking.getCheckInDate();
            LocalDateTime toDate = booking.getCheckOutDate();
            String formattedFromDate = fromDate.format(formatter);
            String formattedToDate = toDate.format(formatter);

            description.append("Full payment of bill ")
                    .append(booking.getId())
                    .append(", Customer ")
                    .append(booking.getUser()
                            .getFullname())
                    .append(", form ")
                    .append(formattedFromDate)
                    .append(" to ").append(formattedToDate);

            String descripstionlast = String.valueOf(description);

            ItemData itemData = ItemData.builder().name(billNamelast).price(totalAmount).quantity(1).build();

            PaymentData paymentData = PaymentData.builder().orderCode(booking.getId()).description(descripstionlast)
                    .amount(totalAmount).item(itemData).returnUrl(linkRequestBody.getReturnUrl())
                    .cancelUrl(linkRequestBody.getCancelUrl()).build();

            CheckoutResponseData data = payOS.createPaymentLink(paymentData);

            return new PaymentPayOSResponse<>(0, "success", data);
        } catch (Exception e) {
            e.printStackTrace();
            return new PaymentPayOSResponse(-1, "fail", null);
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

    public PaymentPayOSResponse confirmWebhook( Map<String, String> requestBody) {
       
        try {
            String str = payOS.confirmWebhook(requestBody.get("webhookUrl"));
            
            return new PaymentPayOSResponse<>(0, "ok", str);
        } catch (Exception e) {
            e.printStackTrace();
            return new PaymentPayOSResponse(-1, e.getMessage(), null);
        }
    }
}
