package com.example.stagealarm.user.dto;

import com.example.stagealarm.user.entity.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private LocalDateTime createdAt;

    private String loginId;
    private String password;
    private String email;
    private String gender;
    private String phone;
    private String profileImg;
    private String address;

    public static UserDto fromEntity(UserEntity userEntity){
        return UserDto.builder()
                .id(userEntity.getId())
                .createdAt(userEntity.getCreatedAt())
                .loginId(userEntity.getLoginId())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .gender(userEntity.getGender())
                .phone(userEntity.getPhone())
                .profileImg(userEntity.getProfileImg())
                .address(userEntity.getAddress())
                .build();
    }

}
