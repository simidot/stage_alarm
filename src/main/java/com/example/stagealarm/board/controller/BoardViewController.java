package com.example.stagealarm.board.controller;

import com.example.stagealarm.board.dto.BoardDto;
import com.example.stagealarm.board.service.BoardService;
import com.example.stagealarm.board.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardViewController {
  private final BoardService boardService;
  private final CategoryService categoryService;

}
