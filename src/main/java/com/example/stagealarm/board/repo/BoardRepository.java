package com.example.stagealarm.board.repo;

import com.example.stagealarm.board.entity.Board;
import com.example.stagealarm.board.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

  @Query("SELECT b " +
    "FROM Board b " +
    "WHERE b.category.id = :categoryId")
  Page<Board> findAllByCategoryId(Long categoryId, Pageable pageable);
}
