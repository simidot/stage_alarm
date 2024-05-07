package com.example.stagealarm.board.dto;

import com.example.stagealarm.board.entity.ActivateEnum;
import com.example.stagealarm.board.entity.Board;
import com.example.stagealarm.board.entity.BoardComment;
import com.example.stagealarm.board.entity.Category;
import com.example.stagealarm.image.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


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
  private Long categoryId;
  private String createdAt;
  private List<Image> imageList;
  private List<BoardCommentDto> commentList;

  public static BoardDto fromEntity(Board entity) {
    List<BoardCommentDto> commentDtos = new ArrayList<>();
    if (!entity.getCommentList().isEmpty()) {
      for (BoardComment comment : entity.getCommentList()) {
        // 댓글만 넣기
        if (comment.getDepth() == 0)
          commentDtos.add(convertToDto(comment));
      }
    }

    LocalDateTime createdAt = entity.getCreatedAt();
    LocalDate today = LocalDate.now();
    String formattedDate;
    // 오늘 생성된 게시물인 경우 날짜와 시간 모두 표시
    if (createdAt.toLocalDate().isEqual(today)) {
      formattedDate = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    } else {
      // 어제 이전에 생성된 게시물인 경우 날짜만 표시
      formattedDate = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    return new BoardDto(
      entity.getId(),
      entity.getTitle(),
      entity.getContent(),
      entity.getActivate(),
      entity.getViews(),
      entity.getUserEntity().getId(),
      entity.getUserEntity().getLoginId(),
      entity.getCategory().getId(),
      formattedDate,
      entity.getImageList(),
      commentDtos
    );
  }

  // BoardComment 엔티티를 BoardCommentDto로 변환
  private static BoardCommentDto convertToDto(BoardComment comment) {
    List<BoardCommentDto> childCommentDtos = new ArrayList<>();
    for (BoardComment childComment : comment.getChildComments()) {
      childCommentDtos.add(convertToDto(childComment));
    }

    return new BoardCommentDto(
      comment.getId(),
      comment.getContent(),
      comment.getDepth(),
      comment.getUserEntity().getId(),
      comment.getUserEntity().getLoginId(),
      comment.getBoard().getId(),
      BoardCommentDto.formattedDate(comment),
      childCommentDtos
    );
  }
}
