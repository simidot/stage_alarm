package com.example.stagealarm.board.repo;

import com.example.stagealarm.board.entity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<BoardComment, Long> {
}
