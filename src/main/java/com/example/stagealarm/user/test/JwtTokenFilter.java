package com.example.stagealarm.user.test;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
  private final JwtTokenUtils jwtTokenUtils;
  private final JpaUserService manager;

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
    if (authHeader != null && authHeader.startsWith("Bearer")) {
      String token = authHeader.split(" ")[1];

      // 3. Token이 유효한 토큰인지 검사
      if (jwtTokenUtils.validate(token)) {

        // 4. 유효하다면 해당 토큰을 바탕으로 사용자 정보를 SecurityContext에 등록
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // 사용자 정보 회수
        String loginId = jwtTokenUtils
          .parseClaims(token)
          .getSubject();

        // getAuthorities 메소드의 결과에 따라서 사용자 권한을 확인
        CustomUserDetails customUserDetails = manager.loadUserByloginId(loginId);
        log.info("authority: {}", customUserDetails.getAuthority());
        log.info("loginId: {}", customUserDetails.getLoginId());

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
      } else {
        log.warn("jwt validation failed");
      }
    }

    // 5. 다음 필터 호출
    filterChain.doFilter(request, response);
  }
}
