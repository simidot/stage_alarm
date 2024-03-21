package com.example.stagealarm.board.repo;

import com.example.stagealarm.board.dto.ContentSearchParams;
import com.example.stagealarm.board.dto.TitleSearchParams;
import com.example.stagealarm.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QBoardRepo {
  // 게시판 제목 검색
  Page<Board> searchTitle(TitleSearchParams params, Pageable pageable);

  // 게시판 내용 검색
  Page<Board> searchContent(ContentSearchParams params, Pageable pageable);
}
