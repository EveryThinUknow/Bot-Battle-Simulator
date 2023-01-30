package com.bbs.backend.service.impl.utils;

import com.bbs.backend.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override //一般为true
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override //该用户是否没有被锁定，一般没有，true
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override //该用户授权是否过期，一般为true
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override //该用户是否正被启用，一般为true
    public boolean isEnabled() {
        return true;
    }
}
