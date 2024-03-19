package com.example.stagealarm.show.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ShowInfoDto {
    private LocalDate date;
    private LocalDateTime startTime;
    private Integer hours;
    private Integer duration;
    private String location;
    private String title;
    private String ticketVendor;
    private String price;
}
