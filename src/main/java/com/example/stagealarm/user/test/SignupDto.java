package com.example.stagealarm.user.test;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@Builder
public class SignupDto {
  private Long id;

  @Column(nullable = false)
  private String loginId;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false)
  private String checkPassword;

  private String username;
  private String email;
  private String phone;

  private UserAuthority authority;
}
