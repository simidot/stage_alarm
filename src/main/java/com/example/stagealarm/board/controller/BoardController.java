package com.example.stagealarm.board.controller;

import com.example.stagealarm.board.dto.BoardDto;
import com.example.stagealarm.board.dto.BoardListDto;
import com.example.stagealarm.board.dto.ContentSearchParams;
import com.example.stagealarm.board.dto.TitleSearchParams;
import com.example.stagealarm.board.service.BoardService;
import com.example.stagealarm.board.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Board 컨트롤러", description = "Board API입니다.")
@Slf4j
@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {
  private final BoardService boardService;
  private final CategoryService categoryService;

  // Create
  @PostMapping
  public BoardDto write(
//   @RequestBody BoardDto dto
    @RequestBody List<MultipartFile> files,
    @RequestPart BoardDto dto
  ) {
    return boardService.createBoard(files, dto);
  }

  // Read
  // read All + Sort
  @GetMapping("/{categoryId}")
  public Page<BoardListDto> readAll(
    @PathVariable("categoryId") Long categoryId,
    @RequestParam(value = "sortParam", defaultValue = "dateD") String sortParam, // Front에서 dateD, dateA, viewsD, viewsA를 건네준다.
    Pageable pageable
  ) {
    return categoryService.readAll(categoryId, sortParam, pageable);
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
    @RequestBody List<MultipartFile> files,
    @RequestBody List<String> existingImages,
    @RequestPart BoardDto dto
  ) {
    return boardService.reWriteBoard(files, existingImages, boardId, dto);
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
