package com.example.stagealarm.artist.controller;

import com.example.stagealarm.artist.dto.ArtistSubscribeDto;
import com.example.stagealarm.artist.service.ArtistSubscribeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ArtistSubscribe 컨트롤러", description = "ArtistSubscribe API입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/artists/{id}/subscribe")
public class ArtistSubscribeController {
  private final ArtistSubscribeService subscribeService;

  @PostMapping
  public ResponseEntity<ArtistSubscribeDto> subscribe(
          @PathVariable("id") Long artistId
  ) {
    return ResponseEntity.ok(subscribeService.subscribeArtist(artistId));
  }
}
