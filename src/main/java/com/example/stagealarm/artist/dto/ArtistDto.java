package com.example.stagealarm.artist.dto;

import com.example.stagealarm.artist.entity.Artist;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDto {

    private Long id;
    private LocalDateTime createdAt;
    private String name;
    private Integer age;
    private String gender;
    private String profileImg;
    private Long likes;
    private Boolean isLiked;

    public static ArtistDto fromEntity(Artist artist){
        return ArtistDto.builder()
                .id(artist.getId())
                .createdAt(artist.getCreatedAt())
                .name(artist.getName())
                .age(artist.getAge())
                .gender(artist.getGender())
                .profileImg(artist.getProfileImg())
                .likes((long) artist.getLikes().size())
                .isLiked(false)
                .build();
    }

    public static ArtistDto fromEntityWithLikeStatus(Artist artist, boolean isLiked) {
        return ArtistDto.builder()
                .id(artist.getId())
                .createdAt(artist.getCreatedAt())
                .name(artist.getName())
                .age(artist.getAge())
                .gender(artist.getGender())
                .profileImg(artist.getProfileImg())
                .likes((long) artist.getLikes().size())
                .isLiked(isLiked)
                .build();
    }
}
