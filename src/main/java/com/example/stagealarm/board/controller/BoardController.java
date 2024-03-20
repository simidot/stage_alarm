package com.example.stagealarm.board.controller;

import com.example.stagealarm.board.dto.BoardDto;
import com.example.stagealarm.board.dto.ContentSearchParams;
import com.example.stagealarm.board.dto.TitleSearchParams;
import com.example.stagealarm.board.service.BoardService;
import com.example.stagealarm.board.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {
  private final BoardService boardService;
  private final CategoryService categoryService;

  // Create
  // todo 이미지 넣기
  @PostMapping
  public BoardDto write(BoardDto dto) {
    return boardService.createBoard(dto);
  }

  // Read
  // read All + Sort
  @GetMapping("/{category}")
  public Page<BoardDto> readAll(
    @PathVariable("category") String category,
    @RequestParam(value = "sort", defaultValue = "desc") String sortParam,
    Pageable pageable
  ) {
    return  categoryService.readAll(category, sortParam, pageable);
  }

  // read One
  @GetMapping("/detail/{boardId}")
  public BoardDto readOne(
    @PathVariable("boardId") Long boardId
  ) {
    return boardService.readOne(boardId);
  }

  // Update
  @PutMapping("/rewriting/{boardId}")
  public BoardDto rewrite(
    @PathVariable("boardId") Long boardId,
    @RequestBody BoardDto dto
  ) {
    return boardService.updateBoard(boardId, dto);
  }

  // Delete
  @DeleteMapping("/trash/{boardId}")
  public void erase(
    @PathVariable("boardId") Long boardId
  ) {
    boardService.deleteBoard(boardId);
  }

  // Search
    // title
  @GetMapping("/title")
  public Page<BoardDto> searchTitle(
    TitleSearchParams params,
    Pageable pageable
  ) {
    log.info("{}", params);
    return boardService.searchTitle(params, pageable);
  }

    // content
  @GetMapping("/content")
  public Page<BoardDto> searchContent(
    ContentSearchParams params,
    Pageable pageable
  ) {
    log.info("{}", params);
    return boardService.searchContent(params, pageable);
  }
}
