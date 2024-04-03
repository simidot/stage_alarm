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
    private Long loginUserId;
    private ShowComments parentComment;

    public static ShowCommentsResponseDto fromEntity(ShowComments showComments, Long loginUserId) {

        return ShowCommentsResponseDto.builder()
                .id(showComments.getId())
                .content(showComments.getContent())
                // userEntity(showComments.getUserEntity()) 여기서 lazy로딩으로 가져옴
                // 댓글 작성한 유저의 id
                // proxy 객체를 dto 객체로 변환
                .userResponseDto(UserResponseDto.fromEntity(showComments.getUserEntity()))
                .showInfoId(showComments.getShowInfo().getId())
                // 현재 로그인한 유저의 id
                .loginUserId(loginUserId)
                .parentComment(showComments.getParentComment())
                .build();
    }
}
