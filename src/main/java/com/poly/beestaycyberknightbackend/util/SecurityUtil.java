package com.poly.beestaycyberknightbackend.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import com.poly.beestaycyberknightbackend.dto.response.CustomUserDetails;

@Service
public class SecurityUtil {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    @Value("${security.authentication.jwt.base64-secret}")
    private String jwtKey;

    @Value("${security.authentication.jwt.token-validity-in-seconds}")
    private long jwtKeyExpiration;

    public SecurityUtil(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String createToken(Authentication authentication) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.jwtKeyExpiration, ChronoUnit.SECONDS);

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> beestayClaims = new HashMap<>();
        beestayClaims.put("id", customUserDetails.getUserId());
        beestayClaims.put("fullname", customUserDetails.getFullname());
        beestayClaims.put("email", customUserDetails.getUsername());
        beestayClaims.put("phone", customUserDetails.getPhone());
        beestayClaims.put("cccd", customUserDetails.getCccd());
        beestayClaims.put("rank", customUserDetails.getRankName());
        beestayClaims.put("point", customUserDetails.getPoint());
        beestayClaims.put("authorities", List.of("ROLE_" + customUserDetails.getRoleName())); // Spring dùng key này

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(authentication.getName())
                .claim("Beestay", beestayClaims)
                .claim("authorities", List.of("ROLE_" + customUserDetails.getRoleName()))
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public Jwt decodeToken(String token) {
        return jwtDecoder.decode(token);
    }
}
