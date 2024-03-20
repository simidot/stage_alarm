package com.example.stagealarm.user.test;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
  private Long id;

  private String loginId;
  private String password;

  private String username;
  private String nickname;
  private String email;
  private String ageRange;
  private String phone;
  private String profile;

  private UserAuthority authority;

  private String businessNumber;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    String role = authority.getAuthority();
    return Collections.singletonList(new SimpleGrantedAuthority(role));
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

// --------------------------------------------------------------------------------

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }
}
