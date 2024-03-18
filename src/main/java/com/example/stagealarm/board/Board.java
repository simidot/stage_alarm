package com.example.stagealarm.board;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.image.entity.Image;
import com.example.stagealarm.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Board  extends BaseEntity {
  @Column(nullable = false)
  private String title;
  private String content;
  private String activate; //todo: Enum 신고로 바뀌었을 경우를 대비하여
  private Long views;

  @ManyToOne(fetch = FetchType.LAZY)
  private UserEntity userEntity;
  @ManyToOne(fetch = FetchType.LAZY)
  private Category category;

  @OneToMany(cascade = CascadeType.ALL)
  private List<Image> imageList;

}
