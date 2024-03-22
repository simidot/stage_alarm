package com.example.stagealarm.board.dto;

import com.example.stagealarm.board.entity.BoardComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
  private List<BoardCommentDto> childComments;

  public static BoardCommentDto fromEntity(BoardComment entity) {
    List<BoardCommentDto> childDtos = entity.getChildComments().stream()
      .map(BoardCommentDto::fromEntity)
      .collect(Collectors.toList());

    return new BoardCommentDto(
      entity.getId(),
      entity.getContent(),
      entity.getDepth(),
      entity.getUserEntity().getId(),
      entity.getUserEntity().getLoginId(),
      entity.getBoard().getId(),
      childDtos
    );
  }

  public static BoardCommentDto fromEntityWithChildren(BoardComment entity) {
    List<BoardCommentDto> childDtos = entity.getChildComments().stream()
      .map(BoardCommentDto::fromEntityWithChildren)
      .collect(Collectors.toList());

    return new BoardCommentDto(
      entity.getId(),
      entity.getContent(),
      entity.getDepth(),
      entity.getUserEntity().getId(),
      entity.getUserEntity().getLoginId(),
      entity.getBoard().getId(),
      childDtos
    );
  }
}
