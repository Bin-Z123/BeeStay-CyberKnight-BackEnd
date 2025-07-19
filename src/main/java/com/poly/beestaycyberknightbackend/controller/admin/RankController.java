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
        return response;
    }

    @GetMapping("/list")
    public ApiResponse<List<Rank>> getRanks() {
        ApiResponse response = new ApiResponse<>();
        response.setData(rankService.getRanks());
        response.setCode(200);
        return response;
    }

    @GetMapping("/{id}")
    public ApiResponse<Rank> getRank(@PathVariable Integer id) {
        ApiResponse response = new ApiResponse<>();
        response.setCode(200);
        response.setData(rankService.getRank(id));
        return response;
    }
    
    
    @PutMapping("/{id}")
    public ApiResponse<Rank> updateRank(@PathVariable Integer id, @RequestBody RankRequest request) {
        ApiResponse response = new ApiResponse<>();
        response.setCode(200);
        response.setData(rankService.updateRank(id, request));
        return response;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Rank> deleteRank(@PathVariable Integer id) {
        ApiResponse response = new ApiResponse<>();
        response.setCode(200);
        response.setData(rankService.deleteRank(id));
        return response;
    }


}
