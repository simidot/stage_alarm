package com.example.stagealarm.user.controller;

import com.example.stagealarm.jwt.JwtResponseDto;
import com.example.stagealarm.jwt.JwtTokenUtils;
import com.example.stagealarm.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RestAuthController {

    private final UserService userService;
    @PostMapping("/auth/refresh")
    public JwtResponseDto refreshToken(HttpServletRequest request){
        // 쿠키에있는 토큰 UUID 꺼내기
        String refreshTokenId = JwtTokenUtils.extractCookieValue(request, "refreshTokenId");
        return userService.reissueToken(refreshTokenId);
    }

    @GetMapping("/admin")
    public void admin(){

    }

    @GetMapping("/authenticated")
    public void authentication(){

    }


}
