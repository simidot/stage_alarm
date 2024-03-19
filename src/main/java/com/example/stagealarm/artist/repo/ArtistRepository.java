package com.example.stagealarm.artist.repo;

import com.example.stagealarm.artist.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

}
