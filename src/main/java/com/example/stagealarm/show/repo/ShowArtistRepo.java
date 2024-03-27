package com.example.stagealarm.show.repo;

import com.example.stagealarm.show.entity.ShowArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowArtistRepo extends JpaRepository<ShowArtist, Long> {
}
