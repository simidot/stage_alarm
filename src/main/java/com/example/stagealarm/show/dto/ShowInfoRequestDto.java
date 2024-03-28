package com.example.stagealarm.show.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ShowInfoRequestDto {
    private LocalDate date;
    private LocalTime startTime;
    private Integer hours;
    private Integer duration;
    private String location;
    private String title;
    private String ticketVendor;
    private String price;

    @Builder.Default
    private List<Integer> artistIds = new ArrayList<>(); // 아티스트는 여러명일 가능성
    @Builder.Default
    private List<Integer> genreIds = new ArrayList<>(); // 공연의 장르는 한가지로 결정.
}
