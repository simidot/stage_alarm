package com.example.stagealarm.show.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDto {
    private String title;
    private LocalDate start;
    private LocalDate end;
    private String color;
    private String url;
}
