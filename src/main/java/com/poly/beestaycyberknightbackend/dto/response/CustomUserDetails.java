package com.poly.beestaycyberknightbackend.dto.response;

import com.poly.beestaycyberknightbackend.domain.User;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class CustomUserDetails implements UserDetails {

    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Extra methods to expose info in JWT or API if needed
    public long getUserId() {
        return user.getId();
    }

    public String getFullname() {
        return user.getFullname();
    }

    public String getPhone() {
        return user.getPhone();
    }

    public String getRoleName() {
        return user.getRole().getRoleName();
    }

    public String getCccd() {
        return user.getCccd();
    }

    public int getPoint() {
        return user.getPoint();
    }

    public String getRankName() {
        return user.getRank() != null ? user.getRank().getNameRank() : null;
    }
}

