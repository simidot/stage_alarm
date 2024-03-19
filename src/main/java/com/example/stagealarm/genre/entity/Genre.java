package com.example.stagealarm.genre.entity;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.genre.dto.GenreDto;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Genre extends BaseEntity {
  private String name;

  public static Genre fromDto(GenreDto genreDto) {
    return Genre.builder()
            .name(genreDto.getName())
            .build();
  }
}
