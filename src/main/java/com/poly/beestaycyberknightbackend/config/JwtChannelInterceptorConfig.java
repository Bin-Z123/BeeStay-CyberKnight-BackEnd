package com.poly.beestaycyberknightbackend.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import com.poly.beestaycyberknightbackend.util.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtChannelInterceptorConfig implements ChannelInterceptor {
    private final SecurityUtil securityUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            log.info("Authorization: " + authHeader);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwtToken = authHeader.substring(7);
                try {
                    Jwt jwt = securityUtil.decodeToken(jwtToken);

                    String email = jwt.getSubject();
                    List<String> authoritiesStrings = jwt.getClaimAsStringList("scope");
                    if (authoritiesStrings == null) {
                        // Nếu không có claim quyền, gán một quyền mặc định hoặc để trống
                        authoritiesStrings = Collections.emptyList();
                    }
                    Collection<? extends GrantedAuthority> authorities = authoritiesStrings.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    Authentication auth = new UsernamePasswordAuthenticationToken(email, null, authorities);

                    accessor.setUser(auth);
                    log.info("Successfully authenticated user '{}' for WebSocket session.: ", email);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }

            }
        }
        return message;
    }
}
