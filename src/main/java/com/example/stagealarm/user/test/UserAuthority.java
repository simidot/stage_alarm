package com.example.stagealarm.user.test;

import lombok.Getter;

@Getter
public enum UserAuthority {
  INACTIVE("비활성 사용자"),
  COMMON("일반 사용자"),
  BUSINESS("사업자 사용자"),
  ADMIN("관리자"),
  PENDING("대기 중");

  private String authority;

  UserAuthority(String authority) {
    this.authority = authority;
  }
}
