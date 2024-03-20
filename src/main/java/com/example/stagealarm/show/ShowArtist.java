package com.example.stagealarm.show;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.artist.Artist;
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
public class ShowArtist extends BaseEntity {
  @ManyToOne(fetch = FetchType.LAZY)
  private ShowInfo showInfo;

  @ManyToOne(fetch = FetchType.LAZY)
  private Artist artist;
}
