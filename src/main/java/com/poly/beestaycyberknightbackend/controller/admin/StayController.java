package com.poly.beestaycyberknightbackend.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.beestaycyberknightbackend.domain.Stay;
import com.poly.beestaycyberknightbackend.dto.request.StayCreationRequest;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.repository.StayRepository;
import com.poly.beestaycyberknightbackend.service.StayService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/admin/stay")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StayController {
    StayService service;

    @PostMapping("/create")
    public ApiResponse<Stay> createStay(@RequestBody StayCreationRequest request) {        
        return new ApiResponse<>(HttpStatus.SC_OK, null, service.createStay(request));
    }
       
}
