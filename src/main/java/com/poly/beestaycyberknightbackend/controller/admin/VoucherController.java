package com.poly.beestaycyberknightbackend.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.poly.beestaycyberknightbackend.domain.Voucher;
import com.poly.beestaycyberknightbackend.dto.request.VoucherRequest;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.service.VoucherService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/admin/voucher")
public class VoucherController {
    VoucherService service;

    @PostMapping("/create")
    public ApiResponse<Voucher> createVoucher(@RequestBody VoucherRequest request) {
        ApiResponse response = new ApiResponse<>(200, null, service.creatVoucher(request));
        return response;
    }

    @GetMapping("/list")
    public ApiResponse<List<Voucher>> getVouchers() {
        ApiResponse response = new ApiResponse<>(200, null, service.getVouchers());
        return response;
    }

    @GetMapping("/{id}")
    public ApiResponse<Voucher> getVoucher(@PathVariable Integer id) {
        ApiResponse response = new ApiResponse<>(200, null, service.getVoucher(id));
        return response;
    }

    @PutMapping("/{id}")
    public ApiResponse<Voucher> updateVoucher(@PathVariable Integer id, @RequestBody VoucherRequest request) {
        ApiResponse response = new ApiResponse<>(200, null, service.updatVoucher(id, request));
        return response;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Voucher> deleteVoucher(@PathVariable Integer id) {
        ApiResponse response = new ApiResponse<>(200, null, service.deleteVoucher(id));
        return response;
    }

}
