package com.example.stagealarm.user.test;

import com.example.stagealarm.user.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
public class JpaUserService implements UserDetailsService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenUtils jwtTokenUtils;

  public JpaUserService(
    UserRepository userRepository,
    PasswordEncoder passwordEncoder,
    JwtTokenUtils jwtTokenUtils
  ) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenUtils = jwtTokenUtils;
  }
  // signup user

  // login user - JWT 토큰 발행
  public JwtResponseDto issueJwt(
    JwtRequestDto dto
  ) {
    // 1. 사용자가 제공한 userId가 저장된 사용자인지 판단
    if (!userExists(dto.getLoginId()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

    CustomUserDetails customUserDetails
      = loadUserByloginId(dto.getLoginId());

    // 2. 비밀번호 대조
    if (!passwordEncoder
      .matches(dto.getPassword(), customUserDetails.getPassword()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

    // 3. JWT 발급
    String jwt = jwtTokenUtils.generateToken(customUserDetails);
    JwtResponseDto response = new JwtResponseDto();
    response.setToken(jwt);
    return response;
  }

  // user 정보 불러오기
  public CustomUserDetails loadUserByloginId(
    String loginId
  ) throws UsernameNotFoundException {
    Optional<UserEntity> optionalUser = userRepository.findByLoginId(loginId);

    if (optionalUser.isEmpty())
      throw new UsernameNotFoundException(loginId);

    UserEntity user = optionalUser.get();

    return CustomUserDetails.builder()
      .id(user.getId())
      .loginId(user.getLoginId())
      .password(user.getPassword())
      .email(user.getEmail())
      .phone(user.getPhone())
      .build();
  }

  public UserEntity getUserEntity() {
    CustomUserDetails customUserDetails
      = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    return userRepository.findByLoginId(customUserDetails.getLoginId())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  public boolean userExists(String loginId) {
    return userRepository.existsByLoginId(loginId);
  }

// -------------------------------------------------------------------------------------

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
  }
}
