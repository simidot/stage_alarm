package com.example.stagealarm.show.entity;

import com.example.stagealarm.BaseEntity;
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
public class ShowLike extends BaseEntity {
  @ManyToOne(fetch = FetchType.LAZY)
  @Setter
  private ShowInfo showInfo;
  @ManyToOne(fetch = FetchType.LAZY)
  private UserEntity userEntity;
}
