package com.example.stagealarm.show;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.board.entity.Comment;
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
public class ShowComments extends BaseEntity {
  private String content;
  private Integer depth;

  @ManyToOne(fetch = FetchType.LAZY)
  private UserEntity userEntity;
  @ManyToOne(fetch = FetchType.LAZY)
  private ShowInfo showInfo;
  @ManyToOne(fetch = FetchType.LAZY)
  private Comment parentComment;
}
