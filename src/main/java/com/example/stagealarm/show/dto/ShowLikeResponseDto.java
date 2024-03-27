package com.example.stagealarm.show.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ShowLikeResponseDto {
    private Long userId;
    private Long showInfoId;
    private Long likes;
}
