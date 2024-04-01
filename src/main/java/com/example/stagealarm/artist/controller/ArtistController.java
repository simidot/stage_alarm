package com.example.stagealarm.artist.controller;

import com.example.stagealarm.artist.dto.ArtistDto;
import com.example.stagealarm.artist.dto.ArtistRequestDto;
import com.example.stagealarm.artist.dto.ArtistResponseDto;
import com.example.stagealarm.artist.service.ArtistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/artist")
public class ArtistController {
    private final ArtistService artistService;

    // 아티스트 추가
    @PostMapping
    public ResponseEntity<ArtistResponseDto> addArtist(
        @RequestPart("dto")
        ArtistRequestDto dto,
        @RequestPart("file")
        MultipartFile file
    ) {

        // 관리자 권한 일때
        return ResponseEntity.ok(artistService.join(dto, file));
    }

    // 모든 아티스트 조회
    @GetMapping
    public List<ArtistDto> getAllArtist() {
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
    ){
        artistService.deleteById(id);
    }

    // 아티스트 create시 아티스트명 중복 체크 로직
    @PostMapping("/artist-check")
    public boolean checkArtist(@RequestParam("artistName") String artistName) {
        return artistService.artistExists(artistName);
    }
}
