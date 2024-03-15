package com.example.stagealarm.board;

import com.example.stagealarm.BaseEntity;
import jakarta.persistence.Entity;
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
  private Long writer;
  private Integer depth;
  private Long parentCommentId;
  private Long boardId;
}
