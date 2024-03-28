package com.example.stagealarm.alarm.entity;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.artist.entity.ArtistSubscribe;
import com.example.stagealarm.genre.entity.GenreSubscribe;
import com.example.stagealarm.show.entity.ShowInfo;
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
public class Alert extends BaseEntity {
  private String title;
  private String message;

  @ManyToOne(fetch = FetchType.LAZY)
  private ShowInfo showInfo;

  @ManyToOne(fetch = FetchType.LAZY)
  private ArtistSubscribe artistSubscribe;

  @ManyToOne(fetch = FetchType.LAZY)
  private GenreSubscribe genreSubscribe;
}
