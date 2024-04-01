package com.example.stagealarm.artist.dto;

import com.example.stagealarm.artist.entity.Artist;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
    private Long subscribes;
    private Boolean isSubscribed;
    private List<String> genres;

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
            .subscribes((long) artist.getSubscribes().size())
            .isSubscribed(false)
            .genres(artist.getGenresString(artist.getGenres()))
            .build();
    }

    public static ArtistDto fromEntityWithLikeStatusAndSubStatus(Artist artist, boolean isLiked, boolean isSubscribed) {
        return ArtistDto.builder()
            .id(artist.getId())
            .createdAt(artist.getCreatedAt())
            .name(artist.getName())
            .age(artist.getAge())
            .gender(artist.getGender())
            .profileImg(artist.getProfileImg())
            .likes((long) artist.getLikes().size())
            .isLiked(isLiked)
            .subscribes((long)artist.getSubscribes().size())
            .isSubscribed(isSubscribed)
            .genres(artist.getGenresString(artist.getGenres()))
            .build();
    }


}
