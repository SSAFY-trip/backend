package com.ssafy.enjoytrip.login.filter;

import com.ssafy.enjoytrip.login.details.OAuth2UserDetails;
import com.ssafy.enjoytrip.login.domain.Role;
import com.ssafy.enjoytrip.login.util.JWTUtil;
import com.ssafy.enjoytrip.login.util.UtilFunction;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2UserDetails customUserDetails = (OAuth2UserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        Role role = Role.valueOf(auth.getAuthority());

        String temporaryToken = jwtUtil.createJwt("temporary", username, role, 3000L); // 3초 유효기간

        String redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:5173/oauth2/redirect")
                .queryParam("temporaryToken", temporaryToken)
                .build().toUriString();

        response.sendRedirect(redirectUrl);
    }
}
