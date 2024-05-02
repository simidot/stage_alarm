package com.example.stagealarm.genre.controller;

import com.example.stagealarm.genre.dto.GenreSubscribeDto;
import com.example.stagealarm.genre.service.GenreSubscribeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "GenreSubscribe 컨트롤러", description = "GenreSubscribe API입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres/{id}/subscribe")
public class GenreSubscribeController {
    private final GenreSubscribeService subscribeService;

    @PostMapping
    public ResponseEntity<GenreSubscribeDto> subscribe(
            @PathVariable("id") Long genreId
    ) {
        return ResponseEntity.ok(subscribeService.subscribeGenre(genreId));
    }
}
