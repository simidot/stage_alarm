package com.example.stagealarm.genre.dto;

import com.example.stagealarm.genre.entity.Genre;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShowUploadGenreDto {
  private Long id;
  private String name;

  public static ShowUploadGenreDto fromEntity(Genre genre) {
    return ShowUploadGenreDto.builder()
        .id(genre.getId())
        .name(genre.getName())
        .build();
  }
}
