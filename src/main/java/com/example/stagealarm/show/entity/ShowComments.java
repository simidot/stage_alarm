package com.example.stagealarm.show.entity;

import com.example.stagealarm.BaseEntity;


import com.example.stagealarm.user.entity.UserEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ShowComments extends BaseEntity {
  @Setter
  private String content;
  private Integer depth;

  @ManyToOne(fetch = FetchType.LAZY)
  private UserEntity userEntity;
  @ManyToOne(fetch = FetchType.LAZY)
  private ShowInfo showInfo;
  @ManyToOne(fetch = FetchType.LAZY)
  private ShowComments parentComment;
}
