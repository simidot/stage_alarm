package com.example.stagealarm.artist.entity;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.artist.dto.ArtistDto;
import com.example.stagealarm.artist.dto.ArtistRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Artist extends BaseEntity {

  @Column(nullable = false)
  private String name;
  private Integer age;
  private String gender;
  private String profileImg;

  @Builder.Default
  @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY)
  private List<ArtistLike> likes = new ArrayList<>(); //좋아요 개수

  @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY)
  private List<ArtistSubscribe> subscribes = new ArrayList<>();

  @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<ArtistGenre> genres = new ArrayList<>();


  public List<String> getGenresString(List<ArtistGenre> artistGenres) {
    List<String> genres = new ArrayList<>();
    if (!artistGenres.isEmpty()) {
      for (ArtistGenre artistGenre : artistGenres) {
        genres.add(artistGenre.getGenre().getName());
      }
    }
    return genres;
  }

}
