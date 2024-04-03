package com.example.stagealarm.board.controller;


import com.example.stagealarm.board.service.BoardService;
import com.example.stagealarm.board.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardViewController {
  private final CategoryService categoryService;
  private final BoardService boardService;

  @GetMapping("/{categoryId}")
  public String categoryView(
      @PathVariable("categoryId") Long categoryId,
      Model model
  ) {
    model.addAttribute("category", categoryService.readCategoryName(categoryId));
    return "content/board/category";
  }

  @GetMapping("/view/{boardId}")
  public String boardView(
      @PathVariable("boardId") Long boardId,
      Model model
  ) {
    model.addAttribute("categoryId", boardService.findCategoryId(boardId));
    return "content/board/view";
  }

  @GetMapping("/write")
  public String boardWrite() {
    return "content/board/write";
  }

  @GetMapping("/rewriting/{boardId}")
  public String rewriteBoardView(
      @PathVariable("boardId") Long boardId
  ) {
    return "content/board/rewrite";
  }
}
