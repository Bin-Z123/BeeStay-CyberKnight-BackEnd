package com.poly.beestaycyberknightbackend.service;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public void saveOtp(String email, String otp, long minutes) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("otp:" + email, otp, Duration.ofMinutes(minutes));
    }

    public String getOtp(String email) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.get("otp:" + email);
    }

    public void deleteOtp(String email) {
        redisTemplate.delete("otp:" + email);
    }
}
