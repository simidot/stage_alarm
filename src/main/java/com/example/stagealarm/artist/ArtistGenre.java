package com.example.stagealarm.artist;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.genre.entity.Genre;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ArtistGenre extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  private Artist artist;
  @ManyToOne(fetch = FetchType.LAZY)
  private Genre genre;
}
