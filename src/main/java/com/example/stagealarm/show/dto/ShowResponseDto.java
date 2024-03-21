package com.example.stagealarm.show.dto;

import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
public class ShowResponseDto {
    // 게시글 정보와 댓글을 같이 담기 위한 dto
    private ShowInfoResponseDto showInfoResponseDto;
    private List<ShowCommentsResponseDto> showCommentsRequestDtos;
    public ShowResponseDto(ShowInfoResponseDto showInfoResponseDto, List<ShowCommentsResponseDto> ShowCommentsResponseDto) {
        this.showInfoResponseDto = showInfoResponseDto;
        this.showCommentsRequestDtos = ShowCommentsResponseDto;
    }
}
