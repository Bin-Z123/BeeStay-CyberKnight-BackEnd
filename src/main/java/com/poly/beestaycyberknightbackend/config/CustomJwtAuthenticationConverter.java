package com.poly.beestaycyberknightbackend.config;

import com.poly.beestaycyberknightbackend.dto.response.CustomUserDetails;
import com.poly.beestaycyberknightbackend.domain.User;
import com.poly.beestaycyberknightbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomJwtAuthenticationConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

    private final UserService userService;

    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt jwt) {
        String username = jwt.getSubject();
        User user = userService.handleGetUserByUsername(username);

        List<SimpleGrantedAuthority> authorities = ((List<String>) jwt.getClaims().get("authorities"))
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        CustomUserDetails userDetails = new CustomUserDetails(user, authorities);
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }
}
