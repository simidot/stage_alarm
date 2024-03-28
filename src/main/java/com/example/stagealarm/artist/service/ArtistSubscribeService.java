package com.example.stagealarm.artist.service;

import com.example.stagealarm.artist.dto.ArtistSubscribeDto;
import com.example.stagealarm.artist.entity.Artist;
import com.example.stagealarm.artist.entity.ArtistSubscribe;
import com.example.stagealarm.artist.repo.ArtistRepository;
import com.example.stagealarm.artist.repo.ArtistSubscribeRepo;
import com.example.stagealarm.facade.AuthenticationFacade;
import com.example.stagealarm.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArtistSubscribeService {
  private final ArtistSubscribeRepo subscribeRepo;
  private final AuthenticationFacade facade;
  private final ArtistRepository artistRepository;

  @Transactional
  public ArtistSubscribeDto subscribeArtist(Long artistId) {
    //로그인 유저 찾기
    UserEntity userEntity = facade.getUserEntity();
    Artist artist = artistRepository.findById(artistId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "artist not found"));

    ArtistSubscribe artistSubscribe = new ArtistSubscribe();
    // 구독 되어있을 시에는 구독 취소 (해당 데이터 삭제)
    if (userEntity.getArtistSubscribeList().stream()
        .anyMatch(sub -> sub.getArtist().equals(artist))) {
      artistSubscribe = subscribeRepo.findByUserEntityAndArtist(userEntity, artist)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "artist-subscribe not found"));
      subscribeRepo.delete(artistSubscribe);
      artist.getSubscribes().remove(artistSubscribe);
    } else {
      artistSubscribe = ArtistSubscribe.builder()
          .userEntity(userEntity)
          .artist(artist)
          .build();
      subscribeRepo.save(artistSubscribe);
    }
    artistRepository.save(artist);
    return ArtistSubscribeDto.builder()
        .subscribes((long) artist.getSubscribes().size())
        .artistId(artistId)
        .userId(userEntity.getId())
        .build();
  }
}
