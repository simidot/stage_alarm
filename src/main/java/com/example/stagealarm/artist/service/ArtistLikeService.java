package com.example.stagealarm.artist.service;

import com.example.stagealarm.artist.entity.Artist;
import com.example.stagealarm.artist.entity.ArtistLike;
import com.example.stagealarm.artist.dto.ArtistLikeDto;
import com.example.stagealarm.artist.entity.QArtistLike;
import com.example.stagealarm.artist.repo.ArtistLikeRepository;
import com.example.stagealarm.artist.repo.ArtistRepository;
import com.example.stagealarm.artist.repo.QArtistLikeRepo;
import com.example.stagealarm.facade.AuthenticationFacade;
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
    private final QArtistLikeRepo qArtistLikeRepo;
    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final AuthenticationFacade facade;

    @Transactional
    public ArtistLikeDto artistLike(Long artistId) {
        //로그인한 계정 아이디로 유저 찾기
        Long userId = facade.getUserEntity().getId();
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "artist not found"));

        ArtistLikeDto resultDto = new ArtistLikeDto();
        // 좋아요가 이미 있을 시에는 좋아요 취소 (해당 데이터 삭제)
        if (userEntity.getLikeList().stream()
                .anyMatch(like -> like.getArtist().equals(artist))) {
            ArtistLike artistLike = artistLikeRepository.findByUserEntityAndArtist(userEntity, artist)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "artist-like not found"));
            artistLikeRepository.delete(artistLike);
            artist.getLikes().remove(artistLike); //아티스트에서도 좋아요 엔티티 삭제
        } else { // 없을시에는 좋아요 엔티티 생성
            ArtistLike artistLike = ArtistLike.builder()
                    .userEntity(userEntity)
                    .artist(artist)
                    .build();
            artistLike.addArtist(artist); //아티스트에서도 좋아요 엔티티 추가

            artistLikeRepository.save(artistLike);
            resultDto.setUserId(userEntity.getId());
            resultDto.setArtistId(artist.getId());
        }
        artistRepository.save(artist);
        log.info(String.valueOf(artist.getLikes().size()));

        resultDto.setLikes((long) artist.getLikes().size());
        log.info("likes: "+ resultDto.getLikes());
        return resultDto;
    }
    public Long countLikes(Long artistId) {
        return qArtistLikeRepo.getLikesCount(artistId);
    }
}
