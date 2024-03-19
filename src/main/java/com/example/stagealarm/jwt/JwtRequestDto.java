package com.example.stagealarm.jwt;

import lombok.Data;

@Data
public class JwtRequestDto {
    private String loginId;
    private String password;
}
