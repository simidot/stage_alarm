package com.example.stagealarm.artist.dto;

import com.example.stagealarm.artist.entity.Artist;
import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShowUploadArtistDto {
  private Long id;
  private String name;
  private List<String> genres;

  public static ShowUploadArtistDto fromEntity(Artist artist) {
    return ShowUploadArtistDto.builder()
            .id(artist.getId())
            .name(artist.getName())
            .genres(artist.getGenresString(artist.getGenres()))
            .build();
  }

}
