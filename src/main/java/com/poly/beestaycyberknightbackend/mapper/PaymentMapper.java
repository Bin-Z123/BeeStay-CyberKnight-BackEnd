package com.poly.beestaycyberknightbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.poly.beestaycyberknightbackend.domain.Payment;
import com.poly.beestaycyberknightbackend.dto.request.PaymentByCashRequest;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    
    @Mapping(target = "paymentCode", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "paymentDate", ignore = true)
    @Mapping(target = "paymentMethod", ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(target = "paymentType", ignore = true)
    Payment toPayment(PaymentByCashRequest request);
}
