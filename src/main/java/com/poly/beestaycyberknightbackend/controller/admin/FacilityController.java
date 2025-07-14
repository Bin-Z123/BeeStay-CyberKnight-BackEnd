package com.poly.beestaycyberknightbackend.controller.admin;

import org.springframework.web.bind.annotation.RestController;
import com.poly.beestaycyberknightbackend.domain.Facility;
import com.poly.beestaycyberknightbackend.dto.request.FacilityRequest;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.service.FacilityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/admin/facility")
public class FacilityController {
    FacilityService service;

    @PostMapping("/create")
    public ApiResponse<Facility> createFacility(@RequestBody FacilityRequest request) {
        ApiResponse response = new ApiResponse<>(200, null, service.createFacility(request));
        
        return response;
    }

    @GetMapping("/list")
    public ApiResponse<List<Facility>> getFacilites() {
        ApiResponse response = new ApiResponse<>(200, null, service.getFacilities());

        return response;
    }

    @PutMapping("/{id}")
    public ApiResponse<Facility> updateFacility(@PathVariable Long id, @RequestBody FacilityRequest request) {
        ApiResponse response = new ApiResponse<>(200,  null, service.updateFacility(id, request));
        return response;
    }

    @GetMapping("/{id}")
    public ApiResponse<Facility> getFacility(@PathVariable Long id) {
        ApiResponse response = new ApiResponse<>(200, null, service.getFacilityById(id));
        
        return response;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteFacility(@PathVariable Long id){
        ApiResponse response = new ApiResponse<>(200, "delete sucessfully", service.deleteFacility(id));
        return response;
    }
}
