package com.poly.beestaycyberknightbackend.schedule;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.poly.beestaycyberknightbackend.domain.Payment;
import com.poly.beestaycyberknightbackend.repository.PaymentRepository;
import com.poly.beestaycyberknightbackend.service.PayOSService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PayOSCheck {
    static final Logger logger = LoggerFactory.getLogger(PayOSCheck.class);
    final PayOSService payOSService;
    final PaymentRepository paymentRepository;

    @Scheduled(cron = "0 * * * * *")
    public void cancelExpiredPayments() {
        logger.info("--- [CRON JOB] Starting job to cancel expired payments ---");

        // 1. Xác định thời điểm giới hạn là 5 phút trước
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);

        // 2. Tìm tất cả các payment có trạng thái "PENDING" và được tạo trước thời điểm giới hạn
        List<Payment> expiredPayments = paymentRepository
                .findByPaymentStatusAndPaymentDateBefore("PENDING", fiveMinutesAgo);
        
        if(expiredPayments.isEmpty()) {
            logger.info("--- [CRON JOB] No expired payments found ---");
            return;
        }

        // 3. Duyệt qua từng payment và hủy nó
        expiredPayments.forEach(payment -> {
            try {
                logger.warn("Cancelling expired payment with ID: {} created at {}", payment.getId(), payment.getPaymentDate());

                payOSService.cancelOrderByPaymentId(payment.getId());

                logger.info("Successfully cancelled payment with ID: {}", payment.getId());
            } catch (Exception e) {
                // Ghi log lỗi nhưng không dừng job, để nó tiếp tục với các payment khác
                logger.error("Failed to cancel payment with ID: {}. Error: {}", payment.getId(), e.getMessage());
            }
        });
        logger.info("--- [CRON JOB] Finished processing all expired payments. ---");

    }
}
