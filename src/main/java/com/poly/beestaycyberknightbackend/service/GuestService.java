package com.poly.beestaycyberknightbackend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.beestaycyberknightbackend.dto.response.GuestBookingDTO;
import com.poly.beestaycyberknightbackend.mapper.GuestBookingMapper;
import com.poly.beestaycyberknightbackend.repository.GuestBookingRepository;

@Service
public class GuestService {
    @Autowired
    private GuestBookingMapper guestBookingMapper;

    @Autowired 
    private GuestBookingRepository guestBookingRepository;

    public List<GuestBookingDTO> getGuestBookings() {
        return guestBookingRepository.findAll().stream()
                .map(guestBookingMapper::toResp)
                .collect(Collectors.toList());
    }
}
