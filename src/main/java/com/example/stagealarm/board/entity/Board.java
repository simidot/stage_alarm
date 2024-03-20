package com.example.stagealarm.board.entity;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Board  extends BaseEntity {
  @Column(nullable = false)
  @Setter
  private String title;
  @Setter
  private String content;
  @Enumerated(EnumType.STRING)
  @Setter
  private ActivateEnum activate; //todo: Enum 신고로 바뀌었을 경우를 대비하여
  @Setter
  private Long views;

  @ManyToOne(fetch = FetchType.LAZY)
  @Setter
  private Category category;

  @ManyToOne(fetch = FetchType.LAZY)
  private UserEntity userEntity;
}
