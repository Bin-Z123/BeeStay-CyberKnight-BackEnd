package com.poly.beestaycyberknightbackend.controller.admin;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.poly.beestaycyberknightbackend.domain.Facility;
import com.poly.beestaycyberknightbackend.dto.request.FacilityRequest;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.service.FacilityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api")
public class FacilityController {
    FacilityService service;

    @PostMapping("/mana/facility/create")
    public ApiResponse<Facility> createFacility(@RequestPart("facility") FacilityRequest request,
            @RequestPart(required = false) MultipartFile file) {
        System.out.println("Facility: " + request);
        ApiResponse response = new ApiResponse<>(200, "Tạo mới dịch vụ thành công",
                service.createFacility(request, file));

        return response;
    }

    @GetMapping("/facility/list")
    public ApiResponse<List<Facility>> getFacilites() {
        ApiResponse response = new ApiResponse<>(200, "Lấy danh sách dịch vụ thành công", service.getFacilities());

        return response;
    }

    @PutMapping("/mana/facility/{id}")
    public ApiResponse<Facility> updateFacility(@PathVariable Long id,
            @RequestPart("facility") FacilityRequest request,
            @RequestPart(required = false) MultipartFile file) {
        ApiResponse response = new ApiResponse<>(200, "Cập nhật dịch vụ thành công",
                service.updateFacility(id, request, file));
        return response;
    }

    @GetMapping("/facility/{id}")
    public ApiResponse<Facility> getFacility(@PathVariable Long id) {
        ApiResponse response = new ApiResponse<>(200, "Lấy thông tin dịch vụ thành công", service.getFacilityById(id));

        return response;
    }

    @DeleteMapping("/mana/facility/{id}")
    public ApiResponse<?> deleteFacility(@PathVariable Long id) {
        ApiResponse response = new ApiResponse<>(200, "delete sucessfully", service.deleteFacility(id));
        return response;
    }
}
