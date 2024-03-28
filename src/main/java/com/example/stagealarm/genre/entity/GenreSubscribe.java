package com.example.stagealarm.genre.entity;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.user.entity.UserEntity;
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
public class GenreSubscribe extends BaseEntity {
  @ManyToOne(fetch = FetchType.LAZY)
  private Genre genre;
  @ManyToOne
  private UserEntity userEntity;
}
