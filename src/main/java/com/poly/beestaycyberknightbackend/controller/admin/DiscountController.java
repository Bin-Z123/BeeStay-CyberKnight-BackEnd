package com.poly.beestaycyberknightbackend.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.poly.beestaycyberknightbackend.domain.Discount;
import com.poly.beestaycyberknightbackend.dto.request.DiscountRequest;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.service.DiscountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/admin/discount")
public class DiscountController {
    DiscountService discountService;

    @PostMapping("/create")
    public ApiResponse<Discount> createDiscount(@RequestBody DiscountRequest request) {
        ApiResponse response = new ApiResponse<>(200, null, discountService.createDiscount(request));
        return response;
    }

    @GetMapping("/list")
    public ApiResponse<List<Discount>> getDiscounts() {
        ApiResponse response = new ApiResponse<>(200, null, discountService.getDiscounts());
        return response;
    }

    @GetMapping("/{id}")
    public ApiResponse<Discount> getDiscount(@PathVariable Integer id) {
        ApiResponse response = new ApiResponse<>(200, null, discountService.getDiscount(id));
        return response;
    }

    @PutMapping("/{id}")
    public ApiResponse<Discount> updateDiscount(@PathVariable Integer id, @RequestBody DiscountRequest request) {
        ApiResponse response = new ApiResponse<>(200, null, discountService.updateDiscount(id, request));
        return response;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Discount> deleteDiscount(@PathVariable Integer id) {
        ApiResponse response = new ApiResponse<>(200, null, discountService.deleteDiscount(id));
        return response;
    }

    @GetMapping("/discountbyroomtypeid/{id}")
    public ApiResponse<List<Discount>> listDiscountByRoomTypeId(@PathVariable Long id) {
        ApiResponse response = new ApiResponse<>(200, null, discountService.findDiscountByRoomTypeId(id));
        return response;
    }
    
}
