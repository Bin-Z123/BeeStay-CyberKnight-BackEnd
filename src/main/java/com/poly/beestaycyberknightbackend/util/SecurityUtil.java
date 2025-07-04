package com.poly.beestaycyberknightbackend.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.poly.beestaycyberknightbackend.dto.response.CustomUserDetails;
@Service
public class SecurityUtil {
    private final JwtEncoder jwtEncoder;

    public SecurityUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    @Value("${security.authentication.jwt.base64-secret}")
    private String jwtKey;

    @Value("${security.authentication.jwt.token-validity-in-seconds}")
    private long jwtKeyExpiration;

    

    public String createToken(Authentication authentication) {
    Instant now = Instant.now();
    Instant validity = now.plus(this.jwtKeyExpiration, ChronoUnit.SECONDS);

    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

    // Chỉ đẩy những gì cần thiết vào claim
    Map<String, Object> beestayClaims = new HashMap<>();
    beestayClaims.put("id", customUserDetails.getUserId());
    beestayClaims.put("fullname", customUserDetails.getFullname());
    beestayClaims.put("email", customUserDetails.getUsername());
    beestayClaims.put("phone", customUserDetails.getPhone());
    beestayClaims.put("cccd", customUserDetails.getCccd());
    beestayClaims.put("rank", customUserDetails.getRankName());
    beestayClaims.put("point", customUserDetails.getPoint());
    beestayClaims.put("authorities", List.of("ROLE_" + customUserDetails.getRoleName())); // 👈 đây là key mà Spring hiểu

    JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuedAt(now)
        .expiresAt(validity)
        .subject(authentication.getName())
        .claim("Beestay", beestayClaims) // chứa info phụ
        .claim("authorities", List.of("ROLE_" + customUserDetails.getRoleName())) // quyền chính
        .build();

    JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
    return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
}

}
