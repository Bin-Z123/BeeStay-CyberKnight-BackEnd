package com.poly.beestaycyberknightbackend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import com.poly.beestaycyberknightbackend.domain.User;
import com.poly.beestaycyberknightbackend.dto.response.CustomUserDetails;
import com.poly.beestaycyberknightbackend.service.UserService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;
    private final UserService userService;

    public JwtAuthFilter(JwtDecoder jwtDecoder, UserService userService) {
        this.jwtDecoder = jwtDecoder;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Cookie jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> "jwt".equals(cookie.getName()))
                    .findFirst()
                    .orElse(null);

            if (jwtCookie != null) {
                try {
                    Jwt jwt = jwtDecoder.decode(jwtCookie.getValue());
                    String username = jwt.getSubject();

                    User user = userService.handleGetUserByUsername(username);
                    List<SimpleGrantedAuthority> authorities = ((List<String>) jwt.getClaims().get("authorities"))
                            .stream().map(SimpleGrantedAuthority::new).toList();

                    CustomUserDetails userDetails = new CustomUserDetails(user, authorities);

                    var auth = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println(">>> JwtAuthFilter chạy rồi");
                } catch (Exception e) {
                    System.out.println("JWT không hợp lệ: " + e.getMessage());
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
