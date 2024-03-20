package com.example.stagealarm.artist.service;

import com.example.stagealarm.artist.entity.Artist;
import com.example.stagealarm.artist.entity.ArtistLike;
import com.example.stagealarm.artist.dto.ArtistLikeDto;
import com.example.stagealarm.artist.repo.ArtistLikeRepository;
import com.example.stagealarm.artist.repo.ArtistRepository;
import com.example.stagealarm.user.entity.UserEntity;
import com.example.stagealarm.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtistLikeService {
    private final ArtistLikeRepository artistLikeRepository;
    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;

    @Transactional
    public void insertLike(ArtistLikeDto dto) {
        UserEntity userEntity = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        Artist artist = artistRepository.findById(dto.getArtistId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "artist not found"));

        // 이미 좋아요 되어있으면 에러 반환
        if (artistLikeRepository.findByUserEntityAndArtist(userEntity, artist).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user already liked artist");
        }

        ArtistLike like = ArtistLike.builder()
                .userEntity(userEntity)
                .artist(artist)
                .build();
        artistLikeRepository.save(like);
    }

    @Transactional
    public void deleteLike(ArtistLikeDto dto) {
        UserEntity userEntity = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        Artist artist = artistRepository.findById(dto.getArtistId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "artist not found"));
        ArtistLike like = artistLikeRepository.findByUserEntityAndArtist(userEntity, artist)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "like not found"));
        artistLikeRepository.delete(like);
    }
}
