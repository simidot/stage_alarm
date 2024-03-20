package com.example.stagealarm.board.dto;

import com.example.stagealarm.board.entity.ActivateEnum;
import com.example.stagealarm.board.entity.Board;
import com.example.stagealarm.board.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardDto {
  private Long id;
  private String title;
  private String content;
  private ActivateEnum activate;
  private Long views;
  private Long userId;
  private String loginId;
  private Category category;


  public static BoardDto fromEntity(Board entity) {
    return new BoardDto(
      entity.getId(),
      entity.getTitle(),
      entity.getContent(),
      entity.getActivate(),
      entity.getViews(),
      entity.getUserEntity().getId(),
      entity.getUserEntity().getLoginId(),
      entity.getCategory()
    );
  }
}
