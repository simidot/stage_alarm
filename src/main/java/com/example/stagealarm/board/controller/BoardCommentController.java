package com.example.stagealarm.board.controller;

import com.example.stagealarm.board.dto.BoardCommentDto;
import com.example.stagealarm.board.service.BoardCommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "BoardComment 컨트롤러", description = "BoardComment API입니다.")
@Slf4j
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class BoardCommentController {
  private final BoardCommentService boardCommentService;

  // Create
    // comment
  @PostMapping("/{boardId}")
  public BoardCommentDto write(
    @PathVariable("boardId") Long boardId,
    @RequestBody BoardCommentDto dto
  ) {
    return boardCommentService.writeComment(boardId, dto);
  }

    // reply comment
  @PostMapping("/{boardId}/reply/{commentId}")
  public BoardCommentDto replyWrite(
    @PathVariable("boardId") Long boardId,
    @PathVariable("commentId") Long commentId,
    @RequestBody BoardCommentDto dto
  ) {
    return boardCommentService.writeReplyComment(boardId, commentId, dto);
  }

  // Update
  @PutMapping("/rewriting/{commentId}")
  public BoardCommentDto rewrite(
    @PathVariable("commentId") Long commentId,
    @RequestBody BoardCommentDto dto
  ) {
    return boardCommentService.rewriteComment(commentId, dto);
  }

  // Delete
  @DeleteMapping("/trash/{commentId}")
  public void erase(
    @PathVariable("commentId") Long commentId
  ) {
    boardCommentService.deleteComment(commentId);
  }
}
