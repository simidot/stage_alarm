package com.example.stagealarm.board.service;

import com.example.stagealarm.board.dto.BoardDto;
import com.example.stagealarm.board.dto.ContentSearchParams;
import com.example.stagealarm.board.dto.TitleSearchParams;
import com.example.stagealarm.board.entity.ActivateEnum;
import com.example.stagealarm.board.entity.Board;
import com.example.stagealarm.board.repo.BoardRepository;
import com.example.stagealarm.board.repo.QBoardRepo;
import com.example.stagealarm.user.UserEntity;
import com.example.stagealarm.user.test.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
  private final BoardRepository boardRepository;
  private final AuthenticationFacade auth;
  private final QBoardRepo qBoardRepo;


  // Create
  public BoardDto createBoard(BoardDto dto) {
    try {
      // 유저 정보 가져오기
      UserEntity user = auth.getAuth();

      // 생성
      Board newBoard = Board.builder()
        .title(dto.getTitle())
        .content(dto.getContent())
        .activate(ActivateEnum.ACTIVATE)
        .userEntity(user)
        .category(dto.getCategory())
        .build();

      // 저장
      return BoardDto.fromEntity(boardRepository.save(newBoard));
    } catch (Exception e) {
      log.error("err: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "createBoard");
    }
  }

  // Read
  // read One
  public BoardDto readOne(Long boardId) {
    Board targetBoard = boardRepository.findById(boardId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    // 조회 수 올리기
    Long views = targetBoard.getViews() + 1L;
    targetBoard.setViews(views);

    // 반환
    return BoardDto.fromEntity(boardRepository.save(targetBoard));
  }

  // Update
  public BoardDto updateBoard(Long boardId, BoardDto dto) {
    try {
      // 해당 board 가져오기
      Board targetBoard = boardRepository.findById(boardId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

      // 수정 요청자 정보 가져오기
      UserEntity user = auth.getAuth();

      // 권한 확인
      if (!user.getId().equals(targetBoard.getUserEntity().getId()))
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);

      // 수정
      targetBoard.setTitle(dto.getTitle());
      targetBoard.setContent(dto.getContent());
      targetBoard.setCategory(dto.getCategory());

      // 저장 및 반환
      return BoardDto.fromEntity(boardRepository.save(targetBoard));
    } catch (Exception e) {
      log.error("err: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "updateBoard");
    }
  }

  // Delete
  public void deleteBoard(Long boardId) {
    try {
      // 해당 board 가져오기
      Board targetBoard = boardRepository.findById(boardId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

      // 삭제 요청자 정보 가져오기
      UserEntity user = auth.getAuth();

      // 권한 확인
      if (!user.getId().equals(targetBoard.getUserEntity().getId()))
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);

      // 삭제
      boardRepository.deleteById(boardId);
    } catch (Exception e) {
      log.error("err: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "deleteBoard");
    }
  }

  // Search
    // title
  public Page<BoardDto> searchTitle(
    TitleSearchParams params,
    Pageable pageable
  ) {
    if (params.getTitle() == null && params.getCategory() == null)
      // todo readAll pageable 후 수정 필요
      return null;
    return qBoardRepo.searchTitle(params, pageable)
      .map(BoardDto::fromEntity);
  }

    // content
  public Page<BoardDto> searchContent(
    ContentSearchParams params,
    Pageable pageable
  ) {
    if (params.getContent() == null && params.getCategory() == null)
      // todo readAll pageable 후 수정 필요
      return null;
    return qBoardRepo.searchContent(params, pageable)
      .map(BoardDto::fromEntity);
  }
}
