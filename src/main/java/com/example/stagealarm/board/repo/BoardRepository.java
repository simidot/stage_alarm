package com.example.stagealarm.board.repo;

import com.example.stagealarm.board.entity.Board;
import com.example.stagealarm.board.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BoardRepository extends JpaRepository<Board, Long> {
  // CategoryService에서 사용 (카테고리에 따라 게시물들이 달라지므로 CategoryService에서 사용)
  Page<Board> findAllByCategory(Category category, Pageable pageable);
}
