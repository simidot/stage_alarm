package com.example.stagealarm.image;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.board.entity.Board;
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
public class Image extends BaseEntity {
  @Column(nullable = false)
  private String imgUrl;

  @ManyToOne(fetch = FetchType.LAZY)
  private Board board;
}
