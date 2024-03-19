package com.example.stagealarm.artist;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.user.UserEntity;
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

}
