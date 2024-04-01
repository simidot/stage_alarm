package com.example.stagealarm.artist.repo;

import com.example.stagealarm.artist.entity.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QArtistRepo {
  Page<Artist> searchName(String artistName, Pageable pageable);

  Page<Artist> findAll(Pageable pageable);
}
