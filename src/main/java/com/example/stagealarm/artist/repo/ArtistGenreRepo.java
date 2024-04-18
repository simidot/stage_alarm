package com.example.stagealarm.artist.repo;

import com.example.stagealarm.artist.entity.Artist;
import com.example.stagealarm.artist.entity.ArtistGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistGenreRepo extends JpaRepository<ArtistGenre, Long> {
  void deleteByArtist(Artist artist);

}
