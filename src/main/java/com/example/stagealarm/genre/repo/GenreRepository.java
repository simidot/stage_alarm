package com.example.stagealarm.genre.repo;

import com.example.stagealarm.genre.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
