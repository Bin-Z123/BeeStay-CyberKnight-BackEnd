package com.poly.beestaycyberknightbackend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.poly.beestaycyberknightbackend.domain.TransactionLog;
import com.poly.beestaycyberknightbackend.domain.User;
import com.poly.beestaycyberknightbackend.exception.AppException;
import com.poly.beestaycyberknightbackend.exception.ErrorCode;
import com.poly.beestaycyberknightbackend.repository.TransactionLogRepository;
import com.poly.beestaycyberknightbackend.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TransactionlogService {
    TransactionLogRepository logRepository;
    UserRepository userRepository;
    
    public List<TransactionLog> getTransactionLogs(){
        return logRepository.findAll();
    }

    public List<TransactionLog> getTransactionLogByUser(Long userid){
        User user = userRepository.findById(userid).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<TransactionLog> list = logRepository.findByUser(user);

        if(list == null || list.isEmpty()){
            throw new AppException(ErrorCode.NOT_LOG);
        }

        return list;
    }
}
