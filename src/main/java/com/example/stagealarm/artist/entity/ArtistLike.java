package com.example.stagealarm.artist.entity;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.artist.entity.Artist;
import com.example.stagealarm.user.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ArtistLike extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @Setter
  private Artist artist;
  @ManyToOne(fetch = FetchType.LAZY)
  @Setter
  private UserEntity userEntity;

  public void addArtist(Artist artist) {
    this.setArtist(artist);
    artist.getLikes().add(this);
  }

}
