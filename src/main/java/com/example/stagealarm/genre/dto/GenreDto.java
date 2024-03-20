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

    public static GenreDto fromEntity(Genre genre){
        return GenreDto.builder()
                .id(genre.getId())
                .createdAt(genre.getCreatedAt())
                .name(genre.getName())
                .build();
    }
}
