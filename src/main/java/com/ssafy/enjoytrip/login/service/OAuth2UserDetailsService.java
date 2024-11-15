package com.ssafy.enjoytrip.login.service;

import com.ssafy.enjoytrip.login.Response.GoogleResponse;
import com.ssafy.enjoytrip.login.Response.NaverResponse;
import com.ssafy.enjoytrip.login.Response.OAuth2Response;
import com.ssafy.enjoytrip.login.details.OAuth2UserDetails;
import com.ssafy.enjoytrip.login.domain.Role;
import com.ssafy.enjoytrip.login.domain.User;
import com.ssafy.enjoytrip.login.dto.UserDTO;
import com.ssafy.enjoytrip.login.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

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
        User user = userRepository.findByUsername(username);
        if (user == null) {
            User userEntity = User.oauthUserBuilder()
                    .provider(oAuth2Response.getProvider())
                    .email(oAuth2Response.getEmail())
                    .username(username)
                    .name(oAuth2Response.getName())
                    .role(Role.ROLE_USER)
                    .build();
            userRepository.save(userEntity);

            UserDTO userDTO = UserDTO.builder()
                    .role(Role.ROLE_USER)
                    .username(username)
                    .name(oAuth2Response.getName())
                    .build();

            return new OAuth2UserDetails(userDTO);
        }
        else {
            user.setEmail(oAuth2Response.getEmail());
            user.setName(oAuth2Response.getName());

            userRepository.save(user);
            UserDTO userDTO = UserDTO.builder()
                    .role(user.getRole())
                    .username(user.getUsername())
                    .name(oAuth2Response.getName())
                    .build();
            return new OAuth2UserDetails(userDTO);
        }
    }
}