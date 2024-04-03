package com.example.stagealarm.artist.repo;

import com.example.stagealarm.artist.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

  boolean existsByName(String artistName);
}
