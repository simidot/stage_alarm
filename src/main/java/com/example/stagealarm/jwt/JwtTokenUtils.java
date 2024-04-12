package com.example.stagealarm.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

// JWT 자체와 관련된 기능을 만드는 곳
@Slf4j
@Component
public class JwtTokenUtils {
    // JWT를 만드는 용도의 암호키
    private final Key signingKey;
    // JWT를 해석하는 용도의 객체
    private final JwtParser jwtParser;

    private final StringRedisTemplate redisTemplate;


    public JwtTokenUtils(
            @Value("${jwt.secret}")
            String jwtSecret,
            StringRedisTemplate redisTemplate
    ) {
        this.signingKey
                = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtParser = Jwts
                .parserBuilder()
                .setSigningKey(this.signingKey)
                .build();
        this.redisTemplate = redisTemplate;
    }

    // UserDetails를 받아서 JWT로 변환하는 메서드
    public String generateToken(UserDetails userDetails) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        // JWT에 담고싶은 정보를 Claims로 만든다.

        // 현재 호출되었을 때 epoch time
        Instant now = Instant.now();
        Claims jwtClaims = Jwts.claims()
                // sub: 누구인지
                .setSubject(userDetails.getUsername())
                // iat: 언제 발급 되었는지
                .setIssuedAt(Date.from(now))
                // exp: 언제 만료 예정인지 (한시간)
                .setExpiration(Date.from(now.plusSeconds(60*60)));


        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        jwtClaims.put("roles", authorities);

        // 최종적으로 JWT를 발급한다.
        String jwtToken = Jwts.builder()
                .setClaims(jwtClaims)
                .signWith(this.signingKey)
                .compact();
        operations.set(jwtToken, "true", 1, TimeUnit.HOURS); // 리프레시 토큰 1일간 유효

        return jwtToken;
    }

    // 리프레쉬 토큰 발급
    public Cookie generateRefreshToken(UserDetails userDetails) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();

        // 현재 호출되었을 때 epoch time
        Instant now = Instant.now();
        Claims jwtClaims = Jwts.claims()
                // sub: 누구인지
                .setSubject(userDetails.getUsername())
                // iat: 언제 발급 되었는지
                .setIssuedAt(Date.from(now))
                // exp: 언제 만료 예정인지 (하루)
                .setExpiration(Date.from(now.plusSeconds(60*60*24)));


        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        jwtClaims.put("roles", authorities);

        String refreshToken = Jwts.builder()
                .setClaims(jwtClaims)
                .signWith(this.signingKey)
                .compact();

        // 리프레시 토큰 생성 및 저장 로직
        String refreshTokenId = UUID.randomUUID().toString();
        operations.set(refreshTokenId, refreshToken, 1, TimeUnit.DAYS); // 리프레시 토큰 1일간 유효

        // 클라이언트에게 리프레시 토큰 ID를 쿠키로 전달
        Cookie refreshTokenCookie = new Cookie("refreshTokenId", refreshTokenId);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(60*60*24); // 하루
        refreshTokenCookie.setPath("/"); // 쿠키의 경로를 모든 경로로 설정

        return refreshTokenCookie;
    }

//    // 정상적인 JWT 인지를 판단하는 메서드
//    public boolean validate(String token) {
//        try {
//            // 정상적이지 않은 JWT 라면 예외(Exception)가 발생한다.
//            jwtParser.parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            log.warn("invalid jwt");
//        }
//        return false;
//    }

    // 정상적인 JWT 인지를 판단하는 메서드(레디스)
    public boolean validate(String token) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String value = operations.get(token);

        // 레디스에서 해당 키의 값이 "true" 이면 토큰이 유효함을 나타냄
        return value != null;
    }



    // 실제 데이터(Payload)를 반환하는 메서드
    public Claims parseClaims(String token) {
        return jwtParser
                .parseClaimsJws(token)
                .getBody();
    }

    // 쿠키에서 jwt 토큰 UUID 를 추출하는 메서드
    public static String extractCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
