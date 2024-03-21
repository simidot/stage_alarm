package com.example.stagealarm.artist.repo;

import com.example.stagealarm.artist.entity.Artist;
import com.example.stagealarm.artist.entity.ArtistLike;
import com.example.stagealarm.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistLikeRepository extends JpaRepository<ArtistLike, Long> {
    Optional<ArtistLike> findByUserEntityAndArtist(UserEntity user, Artist artist);
}
