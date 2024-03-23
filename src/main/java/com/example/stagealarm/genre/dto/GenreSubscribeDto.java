package com.example.stagealarm.genre.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GenreSubscribeDto {
    private Long userId;
    private Long genreId;
    private Long subscribes;
}
