package com.example.stagealarm.board.service;

import com.example.stagealarm.board.dto.BoardCommentDto;
import com.example.stagealarm.board.entity.Board;
import com.example.stagealarm.board.entity.BoardComment;
import com.example.stagealarm.board.repo.BoardCommentRepository;
import com.example.stagealarm.board.repo.BoardRepository;
import com.example.stagealarm.facade.AuthenticationFacade;
import com.example.stagealarm.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardCommentService {
  private final AuthenticationFacade auth;
  private final BoardRepository boardRepository;
  private final BoardCommentRepository boardCommentRepository;

  // Create
    // Comment: 댓글 생성
  public BoardCommentDto writeComment(Long boardId, BoardCommentDto dto) {
    // 유저 정보 가져오기
    UserEntity targetUser = auth.getUserEntity();

    // Board 정보 가져오기
    Board targetBoard = boardRepository.findById(boardId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    // 댓글 생성
    BoardComment newComment = BoardComment.customBuilder()
      .content(dto.getContent())
      .depth(0)
      .userEntity(targetUser)
      .board(targetBoard)
      .build(); // 여기서 customBuilder를 사용

    // 저장 및 반환
    return BoardCommentDto.fromEntity(boardCommentRepository.save(newComment));
  }

    // Reply Comment: 대댓글 생성
  public BoardCommentDto writeReplyComment(Long boardId, Long commentId, BoardCommentDto dto) {
    // 유저 정보 가져오기
    UserEntity targetUser = auth.getUserEntity();

    // Board 정보 가져오기
    Board targetBoard = boardRepository.findById(boardId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    // 댓글 정보 가져오기
    BoardComment targetComment = boardCommentRepository.findById(commentId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    // 대댓글 생성
    BoardComment newReplyComment = BoardComment.customBuilder()
      .content(dto.getContent())
      .depth(1)
      .userEntity(targetUser)
      .board(targetBoard)
      .parentComment(targetComment)
      .build();

    // 저장 및 반환
    return BoardCommentDto.fromEntity(boardCommentRepository.save(newReplyComment));
  }

  // Read
  // todo 생성일 기준 오름차순으로 정렬되는지 체크
  public List<BoardCommentDto> readComment(Long boardId) {
    List<BoardComment> comments = boardCommentRepository.findAllByBoard_Id(boardId);

    return comments.stream()
      .map(BoardCommentDto::fromEntity)
      .collect(Collectors.toList());
  }

  // Update
  public BoardCommentDto rewriteComment(Long commentId, BoardCommentDto dto) {
    // 유저 정보 가져오기
    UserEntity targetUser = auth.getUserEntity();

    // 댓글 정보 가져오기
    BoardComment targetComment = boardCommentRepository.findById(commentId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    // 권한 확인하기
    if (!targetUser.getId().equals(targetComment.getUserEntity().getId()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "해당 권한이 없습니다.");

    // 댓글 수정
    targetComment.setContent(dto.getContent());

    // 저장 및 반환
    return BoardCommentDto.fromEntity(boardCommentRepository.save(targetComment));
  }

  // Delete
  public void deleteComment(Long commentId) {
    // 유저 정보 가져오기
    UserEntity targetUser = auth.getUserEntity();

    // 댓글 정보 가져오기
    BoardComment targetComment = boardCommentRepository.findById(commentId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    // 권한 확인하기
    if (!targetUser.getId().equals(targetComment.getUserEntity().getId()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "해당 권한이 없습니다.");

    // 삭제
    boardCommentRepository.deleteById(commentId);
  }
}
