package com.example.stagealarm.alarm.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AlertDto {
    private String title;
    private String email;
    private String message;
}
