package com.example.stagealarm.genre.dto;

import com.example.stagealarm.genre.entity.Genre;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GenreDto {

    private Long id;
    private LocalDateTime createdAt;
    private String name;
    private Long subscribes;
    private Boolean isSubscribed;

    public static GenreDto fromEntity(Genre genre){
        return GenreDto.builder()
                .id(genre.getId())
                .createdAt(genre.getCreatedAt())
                .name(genre.getName())
                .subscribes((long) genre.getSubscribes().size())
                .isSubscribed(false)
                .build();
    }

    public static GenreDto fromEntityWithSubscribeStatus(Genre genre, boolean isSubscribed){
        return GenreDto.builder()
                .id(genre.getId())
                .createdAt(genre.getCreatedAt())
                .name(genre.getName())
                .subscribes((long) genre.getSubscribes().size())
                .isSubscribed(isSubscribed)
                .build();
    }
}
