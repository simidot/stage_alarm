package com.example.stagealarm.artist.controller;

import com.example.stagealarm.artist.dto.*;
import com.example.stagealarm.artist.service.ArtistService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Artist 컨트롤러", description = "Artist API입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/artists")
public class ArtistController {
    private final ArtistService artistService;

    // 아티스트 추가
    @PostMapping
    public ResponseEntity<ArtistResponseDto> addArtist(
            @RequestPart("dto")
            ArtistRequestDto dto,
            @RequestPart(name = "file", required = false)
            MultipartFile file
    ) {
        // 관리자 권한 일때
        return ResponseEntity.ok(artistService.join(dto, file));
    }

    // 모든 아티스트 조회
    @GetMapping
    public Page<ArtistDto> getAllArtistWithPage(PaginationRequest paginationRequest) {
        Pageable pageable = PageRequest.of(
                paginationRequest.getPage(),
                paginationRequest.getSize()
        );
        return artistService.searchAll(pageable);
    }

    @GetMapping("/all")
    public List<ShowUploadArtistDto> getAllArtist() {
        return artistService.searchAll();
    }

    // 아티스트 검색
    @GetMapping("/search")
    public Page<ArtistDto> searchArtistName(
            @RequestParam(name = "param") String param, PaginationRequest paginationRequest
    ) {
        Pageable pageable = PageRequest.of(
                paginationRequest.getPage(),
                paginationRequest.getSize()
        );
        return artistService.searchByArtistName(param, pageable);
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
    @PatchMapping("/{id}")
    public ResponseEntity<ArtistResponseDto> updateArtist(
            @PathVariable("id")
            Long id,
            @RequestPart("dto")
            ArtistRequestDto dto,
            @RequestPart(name = "file", required = false)
            MultipartFile file
    ){
        return ResponseEntity.ok(artistService.update(id,dto, file));
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
