package com.example.stagealarm.show.repo;

import com.example.stagealarm.show.entity.ShowComments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShowCommentsRepository extends JpaRepository<ShowComments, Long> {
    List<ShowComments> findByShowInfoId(Long id);
}
