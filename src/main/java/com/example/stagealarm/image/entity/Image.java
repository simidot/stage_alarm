package com.example.stagealarm.image.entity;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.board.Board;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Image extends BaseEntity {
  @Column(nullable = false)
  private String imgUrl;

  @ManyToOne(fetch = FetchType.LAZY)
  @Setter
  private Board board;


  // 연관관계 편의 메서드
  public void addBoard(Board boardEntity) {
    this.setBoard(boardEntity);
    boardEntity.getImageList().add(this);
  }
}
