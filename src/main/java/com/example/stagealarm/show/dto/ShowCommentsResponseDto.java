package com.example.stagealarm.show.dto;

import com.example.stagealarm.show.entity.ShowComments;
import com.example.stagealarm.show.entity.ShowInfo;
import com.example.stagealarm.user.dto.UserResponseDto;
import com.example.stagealarm.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowCommentsResponseDto {
    private Long id;
    private String content;
    private UserResponseDto userResponseDto;
    private Long showInfoId;
    private ShowComments parentComment;

    public static ShowCommentsResponseDto fromEntity(ShowComments showComments) {
        UserEntity user = showComments.getUserEntity();

        return ShowCommentsResponseDto.builder()
                .id(showComments.getId())
                .content(showComments.getContent())
                .userResponseDto(UserResponseDto.fromEntity(user))
                .showInfoId(showComments.getShowInfo().getId())
                .parentComment(showComments.getParentComment())
                .build();
    }
}
