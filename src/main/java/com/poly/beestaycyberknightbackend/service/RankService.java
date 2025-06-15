package com.poly.beestaycyberknightbackend.service;

import java.util.List;
import java.util.Optional;


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

    public List<Rank> getRanks(){
        return rankRepository.findAll();
    }
    
    public Optional<Rank> getRank(Integer id){
        if (!rankRepository.existsById(id)){
            throw new AppException(ErrorCode.RANK_NOT_EXISTED);
        }
        Optional<Rank> rank = rankRepository.findById(id);
        return rank;
    }

    public Rank updateRank(Integer id, RankRequest request){
        Rank rank = rankRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.RANK_NOT_EXISTED));
        mapper.updateRank(rank, request);
        return rankRepository.save(rank);
    }

    public Rank deleteRank(Integer id){
        Rank rank = rankRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.RANK_NOT_EXISTED));
        rankRepository.delete(rank);
        return rank;
    }

}
