package com.example.stagealarm.genre.service;

import com.example.stagealarm.genre.dto.GenreDto;
import com.example.stagealarm.genre.entity.Genre;
import com.example.stagealarm.genre.repo.GenreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    // 장르 저장
    @Transactional
    public GenreDto createGenre(GenreDto dto) {
        // 장르 중복 검사
        if (this.readAll().stream().anyMatch(g -> dto.getName().equals(g.getName()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Genre already exists");
        }

        Genre genre = Genre.builder()
                .name(dto.getName())
                .build();
        genreRepository.save(genre);
        return GenreDto.fromEntity(genre);
    }

    // 모든 장르 조회
    public List<GenreDto> readAll() {
        return genreRepository.findAll().stream()
                .map(GenreDto::fromEntity)
                .collect(Collectors.toList());
    }

    // id로 장르 조회
    public GenreDto readById(Long id) {
        return GenreDto.fromEntity(genreRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Genre not found")));
    }

    // 장르 수정
    @Transactional
    public GenreDto updateGenre(Long id, GenreDto dto) {
        Genre foundGenre = genreRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Genre not found"));
        // 장르 중복 검사
        if (this.readAll().stream().anyMatch(g -> dto.getName().equals(g.getName()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Genre already exists");
        }
        foundGenre.setName(dto.getName());

        return GenreDto.fromEntity(genreRepository.save(foundGenre));
    }

    // 장르 삭제
    @Transactional
    public void deleteGenreById(Long id) {
        genreRepository.deleteById(id);
    }

}
