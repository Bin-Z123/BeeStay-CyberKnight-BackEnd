package com.poly.beestaycyberknightbackend.controller.admin;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.poly.beestaycyberknightbackend.domain.TransactionLog;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.service.TransactionlogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/admin/log")
public class TransactionLogController {
    TransactionlogService service;
    
    @GetMapping("/list")
    public ApiResponse<List<TransactionLog>> getList() {
        ApiResponse apiResponse = new ApiResponse<>( 200, null, service.getTransactionLogs());
        return apiResponse;
    }
    
    @GetMapping("/userid/{id}")
    public ApiResponse<List<TransactionLog>> getByUser(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse<>( 200, null, service.getTransactionLogByUser(id));
        return apiResponse;
    }
}
