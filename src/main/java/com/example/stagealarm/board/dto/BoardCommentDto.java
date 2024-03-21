package com.example.stagealarm.board.dto;

import com.example.stagealarm.board.entity.BoardComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class BoardCommentDto {
  private Long id;
  private String content;
  private Integer depth;
  private Long userId;
  private String loginId;
  private Long boardId;

  public static BoardCommentDto fromEntity(BoardComment entity) {
    return new BoardCommentDto(
      entity.getId(),
      entity.getContent(),
      entity.getDepth(),
      entity.getUserEntity().getId(),
      entity.getUserEntity().getLoginId(),
      entity.getBoard().getId()
    );
  }
}
