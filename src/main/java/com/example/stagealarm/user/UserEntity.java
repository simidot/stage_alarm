package com.example.stagealarm.user;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.board.Board;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
  private String password;
  @Column(nullable = false)
  private String email; //email로 가입여부 식별

  private String gender;
  private String phone;
  private String profileImg;
  private String address;
}
