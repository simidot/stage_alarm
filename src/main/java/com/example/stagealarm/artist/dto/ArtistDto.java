package com.example.stagealarm.artist.dto;

import com.example.stagealarm.artist.entity.Artist;
import com.example.stagealarm.artist.entity.ArtistGenre;
import com.example.stagealarm.genre.dto.GenreDto;
import com.example.stagealarm.show.dto.AuthorityDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<GenreDto> genres;
    private String authority;

    public static ArtistDto fromEntity(Artist artist){
        List<GenreDto> genreDtos = artist.getGenres().stream()
            .map(artistGenre -> GenreDto.fromEntity(artistGenre.getGenre()))
            .collect(Collectors.toList());

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
            .genres(genreDtos)
            .build();
    }

    public static ArtistDto fromEntityWithLikeStatusAndSubStatus(Artist artist, boolean isLiked, boolean isSubscribed, String authority) {
        List<GenreDto> genreDtos = artist.getGenres().stream()
            .map(artistGenre -> GenreDto.fromEntity(artistGenre.getGenre()))
            .collect(Collectors.toList());

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
            .genres(genreDtos)
            .authority(authority)
            .build();
    }


}
