package com.example.stagealarm.show;

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
public class ShowComments extends BaseEntity {
  private String writer;
  private String content;
  private Integer depth;
  private Long showInfoId;
  private Long userId;
  private Long parentCommentId;
}
