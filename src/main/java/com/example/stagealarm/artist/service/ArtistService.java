package com.example.stagealarm.artist.service;

import com.example.stagealarm.artist.entity.Artist;
import com.example.stagealarm.artist.dto.ArtistDto;
import com.example.stagealarm.artist.repo.ArtistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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


    // 아티스트 저장
    @Transactional
    public ArtistDto join(ArtistDto dto) {
        Artist artist = artistRepository.save(Artist.fromDto(dto));
        return ArtistDto.fromEntity(artist);
    }

    // 모든 아티스트 조회
    public List<ArtistDto> searchAll() {
        return artistRepository.findAll()
                .stream()
                .map(ArtistDto::fromEntity)
                .collect(Collectors.toList());
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
