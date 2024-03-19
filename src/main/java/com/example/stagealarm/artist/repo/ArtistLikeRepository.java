package com.example.stagealarm.artist.repo;

import com.example.stagealarm.artist.Artist;
import com.example.stagealarm.artist.ArtistLike;
import com.example.stagealarm.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistLikeRepository extends JpaRepository<ArtistLike, Long> {
    Optional<ArtistLike> findByUserEntityAndArtist(UserEntity user, Artist artist);
}
