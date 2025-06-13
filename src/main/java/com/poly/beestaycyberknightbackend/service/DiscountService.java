package com.poly.beestaycyberknightbackend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.poly.beestaycyberknightbackend.domain.Discount;
import com.poly.beestaycyberknightbackend.dto.request.DiscountRequest;
import com.poly.beestaycyberknightbackend.exception.AppException;
import com.poly.beestaycyberknightbackend.exception.ErrorCode;
import com.poly.beestaycyberknightbackend.mapper.DiscountMapper;
import com.poly.beestaycyberknightbackend.repository.DiscountRepository;
import com.poly.beestaycyberknightbackend.repository.RoomTypeRepository;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class DiscountService {
    DiscountRepository repository;
    DiscountMapper mapper;
    RoomTypeRepository roomTypeRepository;

    public Discount createDiscount(DiscountRequest request) {
        Discount discount = mapper.toDiscount(request);
        discount.setRoomTypes(request.getRoomTypeIds().stream() // dùng lambda thay cho for để duyệt list RoomTypeId vào
                                                                // discount
                .map(id -> roomTypeRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.ROOMTYPE_NOT_EXISTED)))
                .collect(Collectors.toList()));
        return repository.save(discount);
    }

    public List<Discount> getDiscounts() {
        return repository.findAll();
    }

    public Discount getDiscount(Integer id) {
        return repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DISCOUNT_NOT_EXISTED));
    }

    public Discount updateDiscount(Integer id, DiscountRequest request) {
        Discount discount = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DISCOUNT_NOT_EXISTED));
        mapper.updateDiscount(discount, request);
        discount.setRoomTypes(request.getRoomTypeIds().stream()
                .map(idRT -> roomTypeRepository.findById(idRT)
                        .orElseThrow(() -> new AppException(ErrorCode.ROOMTYPE_NOT_EXISTED)))
                .collect(Collectors.toList()));
        return repository.save(discount);
    }

    @Transactional // nếu lỗi tự động roll back không mất dữ liệu
    public Discount deleteDiscount(Integer id) {
        Discount discount = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DISCOUNT_NOT_EXISTED));
        // Xóa liên kết trong bảng trung gian
        discount.getRoomTypes().forEach(roomType -> roomType.getDiscounts().remove(discount));
        discount.getRoomTypes().clear();
        repository.save(discount); // Cập nhật trạng thái
        repository.delete(discount); // Xóa Discount

        return discount;
    }

    public List<Discount> findDiscountByRoomTypeId(Long id){
        List<Discount> list = repository.findByRoomTypes_Id(id);
        if (list == null || list.isEmpty()) {
            throw new AppException(ErrorCode.ROOMTYPE_NOT_EXISTED);
        }
        return list;
    }
}
