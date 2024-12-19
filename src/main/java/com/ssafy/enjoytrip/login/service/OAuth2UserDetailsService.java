package com.ssafy.enjoytrip.login.service;

import com.ssafy.enjoytrip.login.Response.GoogleResponse;
import com.ssafy.enjoytrip.login.Response.NaverResponse;
import com.ssafy.enjoytrip.login.Response.OAuth2Response;
import com.ssafy.enjoytrip.login.dto.OAuth2UserDetails;
import com.ssafy.enjoytrip.user.domain.Role;
import com.ssafy.enjoytrip.user.domain.User;
import com.ssafy.enjoytrip.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2UserDetailsService extends DefaultOAuth2UserService {
    private final UserMapper userRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else return null;

        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            User userEntity = User.oauthUserBuilder()
                    .provider(oAuth2Response.getProvider())
                    .email(oAuth2Response.getEmail())
                    .username(username)
                    .name(oAuth2Response.getName())
                    .role(Role.ROLE_USER)
                    .build();
            userRepository.save(userEntity);

            return new OAuth2UserDetails(userRepository.findByUsername(username).get());
        }
        else {
            return new OAuth2UserDetails(user.get());
        }
    }
}