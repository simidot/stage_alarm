package com.example.stagealarm.board;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.user.UserEntity;
import jakarta.persistence.Column;
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
public class Board  extends BaseEntity {
  @Column(nullable = false)
  private String title;
  private String content;
  private String activate; //todo: Enum 신고로 바뀌었을 경우를 대비하여
  private Long views;

  @ManyToOne(fetch = FetchType.LAZY)
  private UserEntity userEntity;
  @ManyToOne(fetch = FetchType.LAZY)
  private Category category;
}
