package com.example.stagealarm.user;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.board.Board;
import com.example.stagealarm.user.dto.UserDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {
  @Column(nullable = false, unique = true)
  private String loginId;
  @Column(nullable = false)
  @Setter
  private String password;
  @Column(nullable = false)
  @Setter
  private String email; //email로 가입여부 식별
  @Setter
  private String gender;
  @Setter
  private String phone;
  @Setter
  private String profileImg;
  @Setter
  private String address;
  @Setter
  private String authorities;

  // authorities 빌더에서 값을 정하지 않음
  public static UserEntity fromDto(UserDto dto){
    return UserEntity.builder()
            .loginId(dto.getLoginId())
            .password(dto.getPassword())
            .email(dto.getEmail())
            .gender(dto.getGender())
            .phone(dto.getPhone())
            .profileImg(dto.getProfileImg())
            .address(dto.getAddress())
            .build();
  }
}
