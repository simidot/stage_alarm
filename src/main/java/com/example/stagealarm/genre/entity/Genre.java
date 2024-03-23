package com.example.stagealarm.genre.entity;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.genre.dto.GenreDto;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Genre extends BaseEntity {
  private String name;

  @Builder.Default
  @OneToMany(mappedBy = "genre")
  private List<GenreSubscribe> subscribes = new ArrayList<>();

  public static Genre fromDto(GenreDto genreDto) {
    return Genre.builder()
            .name(genreDto.getName())
            .build();
  }
}
