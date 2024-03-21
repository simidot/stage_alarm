package com.example.stagealarm.genre.controller;

import com.example.stagealarm.genre.dto.GenreSubscribeDto;
import com.example.stagealarm.genre.service.GenreSubscribeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genre/{id}/subscribe")
public class GenreSubscribeController {
    private final GenreSubscribeService subscribeService;

    @PostMapping
    public void subscribe(
            @PathVariable("id") Long genreId,
            @RequestBody GenreSubscribeDto dto
    ) {
        subscribeService.subscribeGenre(dto);
    }
}
