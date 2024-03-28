package com.example.stagealarm.artist.controller;

import com.example.stagealarm.artist.dto.ArtistDto;
import com.example.stagealarm.artist.service.ArtistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping ("/artist")
public class ArtistController {
    private final ArtistService artistService;

    // 아티스트 추가
    @PostMapping
    public ArtistDto addArtist(
            @RequestBody
            ArtistDto dto
    ){
        // 관리자 권한 일때
        return artistService.join(dto);
    }

    // 모든 아티스트 조회
    @GetMapping
    public List<ArtistDto> getAllArtist(){
        return artistService.searchAll();
    }

    // 아티스트 조회
    @GetMapping("/{id}")
    public ArtistDto getArtist(
            @PathVariable("id")
            Long id
    ){
        return artistService.searchById(id);
    }

    // 아티스트 수정
    @PutMapping("/{id}")
    public ArtistDto updateArtist(
            @PathVariable("id")
            Long id,
            @RequestBody
            ArtistDto dto
    ){
        return artistService.update(id, dto);
    }

    // 아티스트 삭제
    @DeleteMapping("/{id}")
    public void deleteArtist(
            @PathVariable("id")
            Long id
    )
    {
        artistService.deleteById(id);
    }
}
