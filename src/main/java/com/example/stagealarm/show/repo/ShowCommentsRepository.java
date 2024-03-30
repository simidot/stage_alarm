package com.example.stagealarm.show.repo;

import com.example.stagealarm.show.entity.ShowComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShowCommentsRepository extends JpaRepository<ShowComments, Long> {
    @Query("SELECT c FROM ShowComments c WHERE c.showInfo.id = :id ORDER BY c.createdAt DESC")
    List<ShowComments> findByShowInfoId(@Param("id") Long id);
}
