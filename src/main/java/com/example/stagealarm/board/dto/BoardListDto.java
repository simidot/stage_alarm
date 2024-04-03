package com.example.stagealarm.board.dto;

import com.example.stagealarm.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@AllArgsConstructor
public class BoardListDto {
  private Long id;
  private String title;
  private String writer;
  private String createdAt;
  private Long views;

  public static BoardListDto fromEntity(Board entity) {
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

    return new BoardListDto(
      entity.getId(),
      entity.getTitle(),
      entity.getUserEntity().getLoginId(),
      formattedDate,
      entity.getViews()
    );
  }
}
