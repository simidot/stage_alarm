package com.example.stagealarm.artist.repo;

import com.example.stagealarm.artist.entity.Artist;
import com.example.stagealarm.artist.entity.ArtistSubscribe;
import com.example.stagealarm.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArtistSubscribeRepo extends JpaRepository<ArtistSubscribe, Long> {
  Optional<ArtistSubscribe> findByUserEntityAndArtist(UserEntity userEntity, Artist artist);

  List<ArtistSubscribe> findByArtistId(Long artistId);
}
