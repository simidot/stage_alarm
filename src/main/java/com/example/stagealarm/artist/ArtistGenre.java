package com.example.stagealarm.artist;

import com.example.stagealarm.BaseEntity;
import jakarta.persistence.Entity;
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
  private Long aristId;
  private Long genreId;
}
