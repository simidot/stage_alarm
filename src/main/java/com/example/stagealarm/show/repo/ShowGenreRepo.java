package com.example.stagealarm.show.repo;

import com.example.stagealarm.show.entity.ShowGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowGenreRepo extends JpaRepository<ShowGenre, Long> {
  List<ShowGenre> findByShowInfoId(Long showInfoId);
}
