package com.example.stagealarm.artist.controller;

import com.example.stagealarm.artist.dto.ArtistLikeDto;
import com.example.stagealarm.artist.service.ArtistLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/artist/{id}/like")
public class ArtistLikeController {
    private final ArtistLikeService likeService;

    @PostMapping
    public ResponseEntity<ArtistLikeDto> likeOrUnlike(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(likeService.artistLike(id));
    }
}
