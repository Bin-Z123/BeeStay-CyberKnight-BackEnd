package com.poly.beestaycyberknightbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.poly.beestaycyberknightbackend.domain.Discount;
import com.poly.beestaycyberknightbackend.dto.request.DiscountRequest;

@Mapper(componentModel = "spring")
public interface DiscountMapper {
    @Mapping(target = "roomTypes", ignore = true)
    Discount toDiscount(DiscountRequest request);

    @Mapping(target = "roomTypes", ignore = true)
    void updateDiscount(@MappingTarget Discount discount, DiscountRequest request);
}
