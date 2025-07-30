package com.poly.beestaycyberknightbackend.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.poly.beestaycyberknightbackend.domain.Stay;
import com.poly.beestaycyberknightbackend.dto.request.StayCreationRequest;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.service.StayService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.util.List;
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
    public ApiResponse<List<Stay>> createStay(@RequestBody List<StayCreationRequest> request) {
        try {
            return new ApiResponse<>(HttpStatus.SC_OK, "Tạo mới stay thành công", service.createMultipleStays(request));
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.SC_OK, e.getMessage(), null);
        }
        
    }

}
