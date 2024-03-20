package com.example.stagealarm.user.test;

import com.example.stagealarm.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
// 사용자 정보를 가져오기 위해 만든 Facade Pattern
public class AuthenticationFacade {
  private final UserRepository userRepository;
  public UserEntity getAuth() {
    CustomUserDetails customUserDetails =
      (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    return userRepository.findByLoginId(customUserDetails.getLoginId())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }
}
