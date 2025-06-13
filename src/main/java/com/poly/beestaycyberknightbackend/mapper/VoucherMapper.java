package com.poly.beestaycyberknightbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.poly.beestaycyberknightbackend.domain.Voucher;
import com.poly.beestaycyberknightbackend.dto.request.VoucherRequest;

@Mapper(componentModel = "spring")
public interface VoucherMapper {

    @Mapping(target = "EStatus", ignore = true)
    Voucher toVoucher(VoucherRequest request);

    @Mapping(target = "EStatus", ignore = true)
    void updatVoucher(@MappingTarget Voucher voucher, VoucherRequest request);
}
