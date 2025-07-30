package com.poly.beestaycyberknightbackend.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.poly.beestaycyberknightbackend.domain.Rank;
import com.poly.beestaycyberknightbackend.dto.request.RankRequest;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;
import com.poly.beestaycyberknightbackend.service.RankService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/admin/rank")
public class RankController {
    RankService rankService;

    @PostMapping("/create")
    public ApiResponse<Rank> createRank(@RequestBody RankRequest request) {
        ApiResponse response = new ApiResponse<>();
        response.setData(rankService.createRank(request));
        response.setCode(HttpStatus.SC_CREATED);
        response.setMessage("Thêm rank thành công");
        return response;
    }

    @GetMapping("/list")
    public ApiResponse<List<Rank>> getRanks() {
        ApiResponse response = new ApiResponse<>();
        response.setData(rankService.getRanks());
        response.setCode(HttpStatus.SC_OK);
        response.setMessage("Lấy danh sách rank thành công");
        return response;
    }

    @GetMapping("/{id}")
    public ApiResponse<Rank> getRank(@PathVariable Integer id) {
        ApiResponse response = new ApiResponse<>();
        response.setCode(HttpStatus.SC_OK);
        response.setData(rankService.getRank(id));
        response.setMessage("Lấy rank thành công");
        return response;
    }
    
    
    @PutMapping("/{id}")
    public ApiResponse<Rank> updateRank(@PathVariable Integer id, @RequestBody RankRequest request) {
        ApiResponse response = new ApiResponse<>();
        response.setCode(HttpStatus.SC_OK);
        response.setData(rankService.updateRank(id, request));
        response.setMessage("Cập nhật rank thành công");
        return response;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Rank> deleteRank(@PathVariable Integer id) {
        ApiResponse response = new ApiResponse<>();
        response.setCode(HttpStatus.SC_OK);
        response.setData(rankService.deleteRank(id));
        response.setMessage("Xóa rank thành công");
        return response;
    }


}
