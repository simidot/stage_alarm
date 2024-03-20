package com.example.stagealarm.board.repo;

import com.example.stagealarm.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
