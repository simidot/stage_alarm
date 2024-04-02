package com.example.stagealarm.show.dto;

import com.example.stagealarm.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDto {
    private String authority;

    public static AuthorityDto fromEntity(UserEntity user) {
        AuthorityDto dto = AuthorityDto.builder()
                .authority(user.getAuthorities())
                .build();

        return dto;
    }
}
