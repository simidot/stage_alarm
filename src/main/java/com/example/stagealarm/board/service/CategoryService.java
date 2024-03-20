package com.example.stagealarm.board.service;

import com.example.stagealarm.board.dto.BoardDto;
import com.example.stagealarm.board.entity.Board;
import com.example.stagealarm.board.entity.Category;
import com.example.stagealarm.board.repo.BoardRepository;
import com.example.stagealarm.board.repo.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryService {
  private final CategoryRepository categoryRepository;
  private final BoardRepository boardRepository;

  public CategoryService(
    CategoryRepository categoryRepository,
    BoardRepository boardRepository
  ) {
    this.categoryRepository = categoryRepository;
    this.boardRepository = boardRepository;

    // Category 생성 (공연 후기, 아티스트, 자유, 동행)
    createCategory("공연 후기");
    createCategory("아티스트");
    createCategory("자유");
    createCategory("동행");
  }

  // Create
  public void createCategory(String category) {
    Category newCategory = Category.builder()
      .category(category)
      .build();

    categoryRepository.save(newCategory);
  }

  // Read
  // read All
  public Page<BoardDto> readAll(String category, String sortParam, Pageable pageable) {
    // categoryId 산출
    Long categoryId = makeCategoryId(category);

    // Sort
    if (sortParam.equals("asc"))
      pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").ascending());

    Page<Board> boardPage
      = boardRepository.findAllByCategoryId(categoryId, pageable);

    return boardPage.map(BoardDto::fromEntity);
  }

  public String returnBoard(String category) {
    Long categoryId = makeCategoryId(category);

    // category 반환 (공연 후기, 아티스트, 자유, 동행)
    return categoryRepository.findById(categoryId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
      .getCategory();
  }

  // String -> Long
  public Long makeCategoryId(String category) {
    Long categoryId = 0L;
    // switch
    switch (category) {
      case "concert":
        categoryId = 1L;
        break;
      case "artist":
        categoryId = 2L;
        break;
      case "agora":
        categoryId = 3L;
        break;
      case "mate":
        categoryId = 4L;
        break;
    }

    return categoryId;
  }
}
