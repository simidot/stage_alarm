package com.example.stagealarm.artist.entity;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.artist.dto.ArtistDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;


@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Artist extends BaseEntity {

  @Column(nullable = false)
  private String name;
  private Integer age;
  private String gender;
  private String profileImg;

  public Artist(Long id) {
    super();
  }

  public static Artist fromDto(ArtistDto artistDto){
    return Artist.builder()
            .name(artistDto.getName())
            .age(artistDto.getAge())
            .gender(artistDto.getGender())
            .profileImg(artistDto.getProfileImg())
            .build();
  }

}
