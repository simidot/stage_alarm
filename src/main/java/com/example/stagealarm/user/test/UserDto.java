package com.example.stagealarm.user.test;

import com.example.stagealarm.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
  private Long id;
  private String loginId;
  private String email;
  private String phone;

  public static UserDto fromEntity(UserEntity entity) {
    return new UserDto(
      entity.getId(),
      entity.getLoginId(),
      entity.getEmail(),
      entity.getPhone()
    );
  }
}
