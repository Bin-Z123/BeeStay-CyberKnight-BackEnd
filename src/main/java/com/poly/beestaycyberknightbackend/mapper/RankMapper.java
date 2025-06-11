package com.poly.beestaycyberknightbackend.mapper;

import org.mapstruct.Mapper;

import com.poly.beestaycyberknightbackend.domain.Rank;
import com.poly.beestaycyberknightbackend.dto.request.RankRequest;

@Mapper(componentModel = "spring")
public interface RankMapper {
    Rank toRank(RankRequest rankRequest);
}
