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
public class Category extends BaseEntity {
  // todo 좀 더 생각 필요
  private String category;
}
