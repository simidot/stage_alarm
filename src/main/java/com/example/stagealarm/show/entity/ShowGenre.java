package com.example.stagealarm.show.entity;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.genre.entity.Genre;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ShowGenre extends BaseEntity {
  @ManyToOne(fetch = FetchType.LAZY)
  private Genre genre;
  @ManyToOne(fetch = FetchType.LAZY)
  @Setter
  private ShowInfo showInfo;
}
