package com.org.sbb2;

import com.org.sbb2.user.SiteUser;
import com.org.sbb2.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String oauthId = oAuth2User.getName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        Map attributesProperties = (Map) attributes.get("properties");
        String nickname = (String) attributesProperties.get("nickname");
        String profileImgUrl = (String) attributesProperties.get("profile_image");

        String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        String username = providerTypeCode + "__%s".formatted(oauthId);

        String email = providerTypeCode + "__%s".formatted(oauthId) +"@kakao.com";

        SiteUser user = userService.whenSocialLogin(providerTypeCode, username, email, nickname, profileImgUrl);

        return new CustomOAuth2User(user.getUsername(), user.getPassword(), user.getGrantedAuthorities());
    }

    class CustomOAuth2User extends User implements OAuth2User {
        public CustomOAuth2User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
            super(username, password, authorities);
        }

        @Override
        public Map<String, Object> getAttributes() {
            return null;
        }

        @Override
        public String getName() {
            return getUsername();
        }
    }
}