package com.example.stagealarm.artist.service;

import com.example.stagealarm.artist.entity.Artist;
import com.example.stagealarm.artist.dto.ArtistDto;
import com.example.stagealarm.artist.repo.ArtistRepository;
import com.example.stagealarm.facade.AuthenticationFacade;
import com.example.stagealarm.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final AuthenticationFacade facade;


    // 아티스트 저장
    @Transactional
    public ArtistDto join(ArtistDto dto) {
        Artist artist = artistRepository.save(Artist.fromDto(dto));
        return ArtistDto.fromEntity(artist);
    }

    // 모든 아티스트 조회
    public List<ArtistDto> searchAll() {
        List<Artist> artists = artistRepository.findAll();

        // 인증된 사용자라면 좋아요한 정보를 함께 전달
        if (facade.getAuth().isAuthenticated()) {
            Long userId = facade.getUserEntity().getId();
            return artists.stream()
                    .map(artist -> {
                        boolean isLiked = artist.getLikes().stream().anyMatch(
                                like -> like.getUserEntity().getId().equals(userId)
                        );
                        return ArtistDto.fromEntityWithLikeStatus(artist, isLiked);
                    })
                    .collect(Collectors.toList());
        } else { // 인증되지 않은 경우는 없이 전달
            return artists.stream()
                    .map(ArtistDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    // id로 아티스트 조회
    public ArtistDto searchById(Long id) {
        return ArtistDto.fromEntity(artistRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        ));
    }

    // 아티스트 수정 로직
    @Transactional
    public ArtistDto update(Long id, ArtistDto dto) {
        Artist artist = artistRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found")
        );

        artist.setName(dto.getName());
        artist.setAge(dto.getAge());
        artist.setGender(dto.getGender());
        artist.setProfileImg(dto.getProfileImg());

        return ArtistDto.fromEntity(artistRepository.save(artist));
    }

    // 아티스트 삭제
    @Transactional
    public void deleteById(Long id){
        artistRepository.deleteById(id);
    }



}
