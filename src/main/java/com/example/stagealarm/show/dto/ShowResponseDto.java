package com.example.stagealarm.show.dto;

import com.example.stagealarm.user.dto.UserResponseDto;
import com.example.stagealarm.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowResponseDto {
    // 게시글 정보와 댓글을 같이 담기 위한 dto
    private ShowInfoResponseDto showInfoResponseDto;
    private List<ShowCommentsResponseDto> showCommentsResponseDtos;
    private AuthorityDto authorityDto;
    public ShowResponseDto(ShowInfoResponseDto showInfoResponseDto, List<ShowCommentsResponseDto> ShowCommentsResponseDto, UserEntity user) {
        this.showInfoResponseDto = showInfoResponseDto;
        this.showCommentsResponseDtos = ShowCommentsResponseDto;
        this.authorityDto = AuthorityDto.fromEntity(user);
    }
}
