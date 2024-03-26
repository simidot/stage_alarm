package com.example.stagealarm.board.entity;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.image.entity.Image;
import com.example.stagealarm.user.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Board  extends BaseEntity {
  @Column(nullable = false)
  @Setter
  private String title;
  @Setter
  private String content;
  @Enumerated(EnumType.STRING)
  @Setter
  private ActivateEnum activate; //todo: Enum 신고로 바뀌었을 경우를 대비하여
  @Setter
  private Long views;

  @ManyToOne(fetch = FetchType.LAZY)
  @Setter
  private Category category;

  @ManyToOne(fetch = FetchType.LAZY)
  private UserEntity userEntity;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<Image> imageList;

  @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
  private List<BoardComment> commentList = new ArrayList<>();

  public void addImage(Image image) {
    // 현재 Board 인스턴스에 Image 객체를 추가
    this.imageList.add(image);
    // Image 객체의 Board 참조를 현재 Board 인스턴스로 설정
    image.setBoard(this);
  }

  public static Board.BoardBuilder customBuilder() {
    return builder()
      .commentList(new ArrayList<>())
      .imageList(new ArrayList<>());
  }
}
