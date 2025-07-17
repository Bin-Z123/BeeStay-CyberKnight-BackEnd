package com.poly.beestaycyberknightbackend.Schedule;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;

import com.poly.beestaycyberknightbackend.service.BookingService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BookingCheck {
    BookingService bookingService;

    @Scheduled(cron = "0 0 0 * * *")
    public void checkBookingCheckinLate() {
        System.out.println("Scheduled is running: " + LocalDateTime.now());
        bookingService.setStatusBookingLate();
    }
}
