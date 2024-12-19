package com.ssafy.enjoytrip.login.dto;

import com.ssafy.enjoytrip.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class OAuth2UserDetails implements OAuth2User {
    private final User user;
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new SimpleGrantedAuthority(user.getRole().name()));
        return collection;
    }
    @Override
    public String getName() {
        return user.getName();
    }
    public String getUsername() {
        return user.getUsername();
    }
    public Long getId() { return user.getId(); }
}