package com.example.stagealarm.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class OAuth2UserServiceImpl
        // 기본적인 OAuth2 인증 과정을 진행해주는 클래스
        extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 어떤 서비스 제공자를 사용했는지
        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();

        // OAuth2 제공자로 부터 받은 데이터를 원하는 방식으로 다시 정리하기 위한 Map
        Map<String, Object> attributes = new HashMap<>();
        String nameAttribute = "";

        // Naver 아이디로 로그인
        if (registrationId.equals("naver")) {
            // Naver에서 받아온 정보다.

            Map<String, Object> responseMap
                    // 네이버가 반환한 JSON에서 response를 회수
                    = oAuth2User.getAttribute("response");

            attributes.put("provider", "naver");
            attributes.put("id", responseMap.get("id"));
            attributes.put("email", responseMap.get("email"));
            attributes.put("nickname", responseMap.get("nickname"));
            attributes.put("name", responseMap.get("name"));
            attributes.put("profileImg", responseMap.get("profile_image"));
            attributes.put("phone", responseMap.get("phone"));
            nameAttribute = "email";
        }

        // Kakao 아이디로 로그인
        if (registrationId.equals("kakao")){
            log.info(oAuth2User.getAttributes().toString());

            // kakao 에서 받아온 정보

            Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
            Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");


            attributes.put("provider", "kakao");
            attributes.put("id", oAuth2User.getAttribute("id"));
            attributes.put("email", kakaoAccount.get("email"));
            attributes.put("nickname", kakaoProfile.get("nickname"));
            attributes.put("profileImg", kakaoProfile.get("profile_image_url"));
            nameAttribute = "email";
        }
        log.info(attributes.toString());
        // 성공시 이 객체를 반환하면 -> OAuth2SuccessHandler 가 실행됨
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                nameAttribute
        );
    }
}







