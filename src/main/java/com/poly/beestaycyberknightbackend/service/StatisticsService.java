package com.poly.beestaycyberknightbackend.service;

import org.springframework.stereotype.Service;


import com.poly.beestaycyberknightbackend.repository.BookingRepository;
import com.poly.beestaycyberknightbackend.repository.RoomRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StatisticsService {
    RoomRepository roomRepository;
    BookingRepository bookingRepository;

    public Double getPercentRoomActive(){
        long totalRooms = roomRepository.count();
        long activeRooms = roomRepository.countByRoomStatus("ACTIVE");
        
        if (totalRooms == 0) {
            return 0.0; 
        }
        double percentActive = (double) activeRooms / totalRooms * 100;

        return Math.round(percentActive * 100.0) / 100.0; 
    }

    public Double getPercentBookingCancel() {
        long totalBookings = bookingRepository.count();
        long canceledBookings = bookingRepository.countByBookingStatus("CANCEL");

        if (totalBookings == 0) {
            return 0.0; 
        }
        double percentCanceled = (double) canceledBookings / totalBookings * 100;

        return Math.round(percentCanceled * 100.0) / 100.0; 
    }
}


