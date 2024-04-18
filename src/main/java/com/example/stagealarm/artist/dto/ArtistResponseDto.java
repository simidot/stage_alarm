package com.example.stagealarm.artist.dto;

import com.example.stagealarm.artist.entity.Artist;
import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ArtistResponseDto {
  private Long id;
  private String name;
  private Integer age;
  private String gender;
  private String profileImg;
  private List<String> genres;

  public static ArtistResponseDto fromEntity(Artist artist){
    return ArtistResponseDto.builder()
            .id(artist.getId())
            .name(artist.getName())
            .age(artist.getAge())
            .gender(artist.getGender())
            .profileImg(artist.getProfileImg())
            .genres(artist.getGenresString(artist.getGenres()))
            .build();
  }
}
