package com.example.stagealarm.user.dto;

import com.example.stagealarm.user.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    // comments에 get요청이 왔을 때 user정보를 주기 위한 userDto(userEntity를 직접 넘기지 않기 위해)
    private String nickname;

    public static UserResponseDto fromEntity(UserEntity user) {
        return UserResponseDto.builder()
                .nickname(user.getNickname())
                .build();
    }
}
