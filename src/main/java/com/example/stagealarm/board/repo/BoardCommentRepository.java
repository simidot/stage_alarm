package com.example.stagealarm.board.repo;

import com.example.stagealarm.board.entity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

  List<BoardComment> findAllByBoard_Id(Long boardId);
}
