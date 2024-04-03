package com.example.stagealarm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PasswordDto {
    private String currentPassword;
    private String newPassword;
}
