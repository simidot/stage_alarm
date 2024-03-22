package com.example.stagealarm.board.controller;

import com.example.stagealarm.board.dto.BoardDto;
import com.example.stagealarm.board.dto.BoardListDto;
import com.example.stagealarm.board.dto.ContentSearchParams;
import com.example.stagealarm.board.dto.TitleSearchParams;
import com.example.stagealarm.board.service.BoardService;
import com.example.stagealarm.board.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {
  private final BoardService boardService;
  private final CategoryService categoryService;

  // Create
  // todo 이미지 넣기
  // note test 완료(이미지는 미완)
  @PostMapping
  public BoardDto write(
   @RequestBody BoardDto dto
  ) {
    return boardService.createBoard(dto);
  }

  // Read
  // read All + Sort
  // note test 완료(이미지는 미완, 생성일 기준 정렬 확인, 조회수 기준 정렬 미완)
  @GetMapping("/{categoryId}")
  public Page<BoardListDto> readAll(
    @PathVariable("categoryId") Long categoryId,
    @RequestParam(value = "sortParam", defaultValue = "desc") String sortParam,
    Pageable pageable
  ) {
    return categoryService.readAll(categoryId, sortParam, pageable);
  }

  // read One
  // note test 완료(이미지는 미완)
  @GetMapping("/detail/{boardId}")
  public BoardDto readOne(
    @PathVariable("boardId") Long boardId
  ) {
    return boardService.readOne(boardId);
  }

  // Update
  // note test 완료(이미지는 미완)
  @PutMapping("/rewriting/{boardId}")
  public BoardDto rewrite(
    @PathVariable("boardId") Long boardId,
    @RequestBody BoardDto dto
  ) {
    return boardService.updateBoard(boardId, dto);
  }

  // Delete
  // note test 완료
  @DeleteMapping("/trash/{boardId}")
  public void erase(
    @PathVariable("boardId") Long boardId
  ) {
    boardService.deleteBoard(boardId);
  }

  // Search
    // title
  // note test 완료
  @GetMapping("/title")
  public Page<BoardDto> searchTitle(
    TitleSearchParams params,
    Pageable pageable
  ) {
    log.info("{}", params);
    return boardService.searchTitle(params, pageable);
  }

    // content
    // note test 완료
  @GetMapping("/content")
  public Page<BoardDto> searchContent(
    ContentSearchParams params,
    Pageable pageable
  ) {
    log.info("{}", params);
    return boardService.searchContent(params, pageable);
  }
}
