package com.poly.beestaycyberknightbackend.service;

import org.springframework.stereotype.Service;
import com.poly.beestaycyberknightbackend.domain.Rank;
import com.poly.beestaycyberknightbackend.dto.request.RankRequest;
import com.poly.beestaycyberknightbackend.exception.AppException;
import com.poly.beestaycyberknightbackend.exception.ErrorCode;
import com.poly.beestaycyberknightbackend.mapper.RankMapper;
import com.poly.beestaycyberknightbackend.repository.RankRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RankService {
    RankRepository rankRepository;
    RankMapper mapper;

    public Rank createRank(RankRequest rankRequest) {
        if (rankRepository.existsBynameRank(rankRequest.getNameRank())){
            throw new AppException(ErrorCode.RANK_EXISTED);
        }
        Rank rank = mapper.toRank(rankRequest);

        return rankRepository.save(rank);
    }
    

}
