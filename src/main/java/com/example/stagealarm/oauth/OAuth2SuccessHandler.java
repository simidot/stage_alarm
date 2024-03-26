package com.example.stagealarm.oauth;

import com.example.stagealarm.jwt.JwtTokenUtils;
import com.example.stagealarm.user.dto.UserDto;
import com.example.stagealarm.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
// OAuth2UserServiceImpl이 성공적으로 OAuth2 과정을 마무리 했을 때,
// 넘겨받은 사용자 정보를 바탕으로 JWT를 생성,
// 클라이언트한테 JWT를 전달
public class OAuth2SuccessHandler
        // 인증에 성공했을 때 특정 URL로 리다이렉트 하고 싶은 경우 활용 가능한 SuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {
    // JWT 발급을 위해 JwtTokenUtils
    private final JwtTokenUtils tokenUtils;
    // 사용자 정보 등록을 위해 UserService
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        // OAuth2UserServiceImpl의 반환값이 할당된다.
        OAuth2User oAuth2User
                = (OAuth2User) authentication.getPrincipal();

        // 넘겨받은 정보를 바탕으로 사용자 정보를 준비
        String email = oAuth2User.getAttribute("email");
        String provider = oAuth2User.getAttribute("provider");
        String username
                = String.format("%s:%s", provider, email);
        String providerId = oAuth2User.getAttribute("id").toString();
        String nickname = oAuth2User.getAttribute("nickname");

        // 이메일 중복 검사
        if (userService.existsByEmail(email)) {
            // 중복된 이메일이 있을 경우, 과거에 가입한 sns 가 아닐시 중복 알림 페이지로 리다이렉트
            String sns= userService.searchByEmail(email).getLoginId().split(":")[0];
            if(!Objects.equals(provider, sns)){
                String duplicateEmailPage = "http://localhost:8080/user/emailDuplicate";
                getRedirectStrategy().sendRedirect(request, response, duplicateEmailPage);
                return;
            }
        }

        // 처음으로 이 소셜 로그인으로 로그인을 시도했다.
        if (!userService.userExists(username)) {
            // 새 계정을 만들어야 한다.
            userService.join(UserDto.builder()
                    .loginId(username)
                    .email(email)
                    .nickname(nickname)
                    .password(passwordEncoder.encode(providerId))
                    .build());
        }
        log.info(username);



        // 데이터베이스에서 사용자 계정 회수
        UserDetails details
                = userService.loadUserByUsername(username);
        // JWT 생성
        String jwt = tokenUtils.generateToken(details);

        // 쿠키에 JWT 토큰 저장
        Cookie jwtCookie = new Cookie("auth_token", jwt); // "auth_token"은 쿠키의 이름입니다.

        // 쿠키 설정 (옵션)
        jwtCookie.setHttpOnly(false); // JavaScript를 통한 접근 방지 false -> 프론트에서 바로 꺼내고 지울용도임
        jwtCookie.setSecure(true); // HTTPS를 통해서만 쿠키를 전송
        jwtCookie.setPath("/"); // 사이트 전역에서 쿠키 접근 가능

        // 응답에 쿠키 추가
        response.addCookie(jwtCookie);
        // oauthClient 는 JWT 를 처리할 클라이언트측 페이지를 연결해줌
        String redirectUrl = "http://localhost:8080/user/oauthClient";
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        // 토큰을 -> 쿠키 -> 리다이렉트
    }
}
