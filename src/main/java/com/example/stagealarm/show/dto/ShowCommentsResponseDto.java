package com.example.stagealarm.show.dto;

import com.example.stagealarm.show.entity.ShowComments;
import com.example.stagealarm.show.entity.ShowInfo;
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
    private UserEntity userEntity;
    private ShowInfo showInfo;
    private ShowComments parentComment;

    public static ShowCommentsResponseDto fromEntity(ShowComments showComments) {

        return ShowCommentsResponseDto.builder()
                .id(showComments.getId())
                .content(showComments.getContent())
                .userEntity(showComments.getUserEntity())
                .showInfo(showComments.getShowInfo())
                .parentComment(showComments.getParentComment())
                .build();
    }
}
