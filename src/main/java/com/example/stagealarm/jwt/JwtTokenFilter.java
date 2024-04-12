package com.example.stagealarm.jwt;

import com.example.stagealarm.user.dto.CustomUserDetails;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;

    public JwtTokenFilter(
            JwtTokenUtils jwtTokenUtils
    ) {
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // 1. Authorization 헤더를 회수
        String authHeader
                = request.getHeader(HttpHeaders.AUTHORIZATION);
        // 2. Authorization 헤더가 존재하는지 + Bearer로 시작하는지
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.split(" ")[1];
            // 3. Token이 유효한 토큰인지
            if (jwtTokenUtils.validate(token)) {
                // 4. 유효하다면 해당 토큰을 바탕으로 사용자 정보를 SecurityContext에 등록
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                // 사용자 정보 회수
                Claims jwtClaims = jwtTokenUtils
                        .parseClaims(token);

                String loginId = jwtClaims.getSubject();
                String authorities = jwtClaims.get("roles", String.class);

                CustomUserDetails customUserDetails = CustomUserDetails.builder()
                        .loginId(loginId)
                        .authorities(authorities)
                        .build();


                // 인증 정보 생성
                AbstractAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                customUserDetails,
                                token,
                                customUserDetails.getAuthorities()
                        );
                // 인증 정보 등록
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
                log.info("set security context with jwt");
            }
            else {
                log.info("access Token Invalid");
                // 액세스 토큰이 유효하지 않을 경우, 리프레시 토큰이 유효한지 확인
                String refreshTokenId = JwtTokenUtils.extractCookieValue(request, "refreshTokenId");
                if (refreshTokenId != null && jwtTokenUtils.validate(refreshTokenId)){
                    // 유효하다면 다시발급을 하기위해 에러 발생
                    log.info("refresh Token valid, reissue a access Token");
                    throw new TokenExpiredException("regenerating jwt token");
                }

                // 유효하지 않다면 그대로 진행
                log.info("refresh Token Invalid");

            }
        }
        // 5. 다음 필터 호출
        // doFilter 를 호출하지 않으면 Controller 까지 요청이 도달하지 못한다.
        filterChain.doFilter(request, response);
    }


}
