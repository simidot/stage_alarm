package com.example.stagealarm.board.entity;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.user.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BoardComment extends BaseEntity {
  @Setter
  private String content;
  private Integer depth; //todo: enum도 괜찮음. 구현에서 정하기

  @ManyToOne(fetch = FetchType.LAZY)
  private UserEntity userEntity;

  @ManyToOne(fetch = FetchType.LAZY)
  private Board board;

  @ManyToOne(fetch = FetchType.LAZY)
  private BoardComment parentComment;

  @OneToMany(mappedBy = "parentComment", fetch = FetchType.LAZY)
  private List<BoardComment> childComments = new ArrayList<>();

  public static BoardComment.BoardCommentBuilder customBuilder() {
    return builder().childComments(new ArrayList<>());
  }
}
