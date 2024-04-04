package com.example.stagealarm.config;


import com.example.stagealarm.jwt.JwtTokenFilter;
import com.example.stagealarm.jwt.JwtTokenUtils;
import com.example.stagealarm.oauth.OAuth2SuccessHandler;
import com.example.stagealarm.oauth.OAuth2UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenUtils jwtTokenUtils;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2UserServiceImpl oAuth2UserService;
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        Customizer<ExceptionHandlingConfigurer<HttpSecurity>> exceptionHandlingCustomizer = exceptionHandling -> {
            exceptionHandling
                    .accessDeniedHandler(new CustomAccessDeniedHandler())
                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
        };
        http
                .exceptionHandling(exceptionHandlingCustomizer)
                // csrf 보안 헤제
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 모든 뷰는 접근 가능하다.
                        .requestMatchers(HttpMethod.GET,"/" // 메인 뷰
                                ,"/artists", // 아티스트 뷰
                                "/boards/{categoryId}", "/boards/view/{boardId}" ,"/boards/write", "/boards/rewriting/{boardId}", // 게시글 뷰
                                "/subscribe", // 장르 뷰
                                "/item/register", "/allItems", "/showItems/{showInfoId}","/item", "/success", "/fail", // 아이템 뷰
                                "/myOrders", // 주문 뷰
                                "/shows", "/shows/{id}", "/shows/showInfo", "/shows/{id}/update", // 공연 뷰
                                "/userInfo", "/user/login", "/user/logout", "/user/signup", "/user/update",
                                "/user/oauthClient", "/user/emailDuplicate", "/user/change-password", "/user/find/id", "/user/find/pw" // 유저 뷰
                                ).permitAll()

                        // 어드민 권한 경로
                        .requestMatchers(HttpMethod.POST, "/admin/alert/send/**", "/admin/artist", "/admin/artist/all", "/artist/artist-check", "/show", "/items", "/admin/genre").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/artist/{id}", "/show/{id}/update", "/items", "/genre/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/artist/{id}", "/show/{id}", "/items/{id}", "/genre/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/user/all", "/user/{id}", "/orders", "/orders/{id}").hasRole("ADMIN")

                        // 유저 권한 경로
                        .requestMatchers(HttpMethod.POST, "/artist/{id}/like", "/artist/{id}/subscribe", "/board", "/comments/{boardId}", "/comments/{boardId}/reply/{commentId}", "/show/{id}", "/orders/{id}/cancel", "/toss/confirm-payment", "/genre/{id}/subscribe").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/board/rewriting/{boardId}", "/comments/rewriting/{commentId}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/user", "/board/trash/{boardId}", "/comments/trash/{commentId}").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/user", "/user/change-password").authenticated()
                        .requestMatchers(HttpMethod.GET, "/orders/{id}/payment", "/user").authenticated()
                        .requestMatchers(HttpMethod.GET, "/myOrder").authenticated()

                        // 비로그인 사용자 권한 경로
                        .requestMatchers(HttpMethod.POST, "/user/login", "/user", "/user/email-check", "/user/email-auth", "/user/email-findId", "/user/loginId-check", "/find/loginId", "/user/signup").permitAll()

                        // 모든 사용자(관리자, 유저, 비로그인) 권한 경로
                        .requestMatchers(HttpMethod.GET, "/artist", "/artist/search", "/artist/{id}", "/board/{categoryId}", "/board/detail/{boardId}", "/board/title", "/board/content", "/show", "/show/{id}", "/show/calender", "/items", "/{showInfoId}/items", "/items/{id}", "/show/{id}/like", "/genre", "/genre/{id}").permitAll()

                        // 기타 모든 요청은 permitAll()
                        .anyRequest()
                        .permitAll()
                )


                // OAuth 가 어떻게 실행하는지
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/user/login")
                        .successHandler(oAuth2SuccessHandler)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService))
                )

                // JWT 사용하기 때문에 보안 관련 세션 해제 (STATELESS: 상태를 저장하지 않음)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(
                        new JwtTokenFilter(jwtTokenUtils),
                        AuthorizationFilter.class
                );

        return http.build();
    }



}
