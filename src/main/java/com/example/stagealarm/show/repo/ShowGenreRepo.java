package com.example.stagealarm.show.repo;

import com.example.stagealarm.show.entity.ShowGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowGenreRepo extends JpaRepository<ShowGenre, Long> {
}
