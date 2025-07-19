package com.poly.beestaycyberknightbackend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.poly.beestaycyberknightbackend.domain.Booking;
import com.poly.beestaycyberknightbackend.domain.Rank;
import com.poly.beestaycyberknightbackend.domain.User;
import com.poly.beestaycyberknightbackend.dto.request.RankRequest;
import com.poly.beestaycyberknightbackend.exception.AppException;
import com.poly.beestaycyberknightbackend.exception.ErrorCode;
import com.poly.beestaycyberknightbackend.mapper.RankMapper;
import com.poly.beestaycyberknightbackend.repository.RankRepository;
import com.poly.beestaycyberknightbackend.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RankService {
    RankRepository rankRepository;
    UserRepository userRepository;
    RankMapper mapper;

    public Rank createRank(RankRequest rankRequest) {
        if (rankRepository.existsBynameRank(rankRequest.getNameRank())) {
            throw new AppException(ErrorCode.RANK_EXISTED);
        }
        Rank rank = mapper.toRank(rankRequest);

        return rankRepository.save(rank);
    }

    public List<Rank> getRanks() {
        return rankRepository.findAll();
    }

    public Optional<Rank> getRank(Integer id) {
        if (!rankRepository.existsById(id)) {
            throw new AppException(ErrorCode.RANK_NOT_EXISTED);
        }
        Optional<Rank> rank = rankRepository.findById(id);
        return rank;
    }

    public Rank updateRank(Integer id, RankRequest request) {
        Rank rank = rankRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RANK_NOT_EXISTED));
        mapper.updateRank(rank, request);
        return rankRepository.save(rank);
    }

    public Rank deleteRank(Integer id) {
        Rank rank = rankRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RANK_NOT_EXISTED));
        rankRepository.delete(rank);
        return rank;
    }

    public void updateUserPointByBookingPaidStatus(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        int totalPoint = user.getBooking().stream()
                .filter(booking -> "PAID".equals(booking.getBookingStatus()))
                .mapToInt(booking -> booking.getTotalAmount() / 100_000)
                .sum();

        user.setPoint(totalPoint);
        userRepository.save(user);
    }

    public void updateRankUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Integer point = user.getPoint();

        Rank matchedRank = rankRepository.findAll()
                .stream()
                .filter(rank -> point >= rank.getMinPointRequired())
                .max((r1, r2) -> Integer.compare(r1.getMinPointRequired(), r2.getMinPointRequired()))
                .orElse(null);

        user.setRank(matchedRank);
        userRepository.save(user);
        userRepository.flush();
    }
}
