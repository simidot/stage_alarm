package com.example.stagealarm.user;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.board.Board;
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
public class Users extends BaseEntity {
  private String loginId;
  private String password;
  private String gender;
  private String email;
  private String phone;
  private String profileImg;
  private String address;
  private List<Board> boards = new ArrayList<>();
}
