package com.example.stagealarm.config;

import com.example.stagealarm.jwt.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        //  Ajax 요청인 경우
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            // 토큰이 만료된 경우 -> 401 (재발급을 위함)
            if(authException instanceof TokenExpiredException){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            }
            // 그외 경우 -> 403
            else{
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            }
            // 그외 요청인 경우
        } else {
            response.sendRedirect("/user/login"); // 기존 사용자에게는 로그인 페이지로 리디렉션
        }
    }
}
