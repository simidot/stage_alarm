package com.example.stagealarm.board.entity;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.user.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {
  private String content;
  private Integer depth; //todo: enum도 괜찮음. 구현에서 정하기

  @ManyToOne(fetch = FetchType.LAZY)
  private UserEntity userEntity;
  @ManyToOne(fetch = FetchType.LAZY)
  private Board board;
  @ManyToOne(fetch = FetchType.LAZY)
  private Comment parentComment;
}
