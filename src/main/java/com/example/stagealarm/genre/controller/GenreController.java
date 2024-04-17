package com.example.stagealarm.genre.controller;

import com.example.stagealarm.genre.dto.GenreDto;
import com.example.stagealarm.genre.dto.ShowUploadGenreDto;
import com.example.stagealarm.genre.service.GenreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Genre 컨트롤러", description = "Genre API입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genre")
public class GenreController {
    private final GenreService genreService;

    // 장르 입력
    @PostMapping
    public GenreDto addGenre(@RequestBody GenreDto dto) {
        // todo: 관리자 권한일 때만 가능
        return genreService.createGenre(dto);
    }

    // 모든 장르 조회 (구독상황 반영)
    @GetMapping
    public List<GenreDto> getAllGenreWithSubscription() {
        return genreService.readAllWithSubscribe();
    }

    @GetMapping("/all")
    public List<ShowUploadGenreDto> getAllGenre() {
        return genreService.readAll();
    }

    // 장르 단일 조회
    @GetMapping("/{id}")
    public GenreDto getGenre(@PathVariable("id") Long id) {
        return genreService.readById(id);
    }

    // 장르 수정
    @PutMapping("/{id}")
    public GenreDto updateGenre(@PathVariable("id") Long id,
                                @RequestBody GenreDto dto
    ) {
        // todo: 관리자 권한일 때 실행
        return genreService.updateGenre(id, dto);
    }

    // 장르 삭제
    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable("id") Long id) {
        // todo: 관리자 권한일 때 실행
        genreService.deleteGenreById(id);
    }
}
