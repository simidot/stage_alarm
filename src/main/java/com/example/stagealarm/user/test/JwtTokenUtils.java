package com.example.stagealarm.user.test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtils {
  private final Key signingKey;
  private final JwtParser jwtParser;

  public JwtTokenUtils(
    @Value("${jwt.secret}")
    String jwtSecret
  ) {
    this.signingKey
      = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

    this.jwtParser = Jwts
      .parserBuilder()
      .setSigningKey(this.signingKey)
      .build();
  }

  public String generateToken(CustomUserDetails userDetails) {
    // 호출되었을 때, epoch time을 받아오는 메서드
    Instant now = Instant.now();

    Claims jwtClaims = Jwts.claims()
      .setSubject(userDetails.getLoginId())
      .setIssuedAt(Date.from(now))
      .setExpiration(Date.from(now.plusSeconds(60 * 60 * 24 * 7)));

    return Jwts.builder()
      .setClaims(jwtClaims)
      .signWith(this.signingKey)
      .compact();
  }

  //todo 향 후 에러 코드 수정
  public boolean validate(String token) {
    try {
      jwtParser.parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      log.warn("invalid jwt");
    }
    return false;
  }

  public Claims parseClaims(String token) {
    return jwtParser
      .parseClaimsJws(token)
      .getBody();
  }
}
