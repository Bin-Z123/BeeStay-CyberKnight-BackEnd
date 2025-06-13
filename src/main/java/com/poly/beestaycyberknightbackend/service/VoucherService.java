package com.poly.beestaycyberknightbackend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.poly.beestaycyberknightbackend.domain.Voucher;
import com.poly.beestaycyberknightbackend.dto.request.VoucherRequest;
import com.poly.beestaycyberknightbackend.exception.AppException;
import com.poly.beestaycyberknightbackend.exception.ErrorCode;
import com.poly.beestaycyberknightbackend.mapper.VoucherMapper;
import com.poly.beestaycyberknightbackend.repository.VoucherRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class VoucherService {
    VoucherRepository repository;
    VoucherMapper mapper;

    public Voucher creatVoucher(VoucherRequest request){
        if (repository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.VOUCHER_EXISTED);
        }

        Voucher voucher = mapper.toVoucher(request);
        voucher.setEStatus(request.isEStatus() ? voucher.getEStatus().ACTIVE : voucher.getEStatus().INACTIVE);

        return repository.save(voucher);
    }

    public List<Voucher> getVouchers(){
        return repository.findAll();
    }

    public Voucher getVoucher(Integer id){
        return repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_EXISTED));
    }

    public Voucher updatVoucher(Integer id ,VoucherRequest request){
        Voucher voucher = repository.findById(id).orElseThrow(()-> new AppException(ErrorCode.VOUCHER_NOT_EXISTED));
        mapper.updatVoucher(voucher, request);
        voucher.setEStatus(request.isEStatus() ? voucher.getEStatus().ACTIVE : voucher.getEStatus().INACTIVE);

        return repository.save(voucher);
        
    }

    public Voucher deleteVoucher(Integer id){
        Voucher voucher = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_EXISTED));
        repository.delete(voucher);
        return voucher;
    }
    
}
