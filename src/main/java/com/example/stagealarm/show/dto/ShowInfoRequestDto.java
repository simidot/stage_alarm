package com.example.stagealarm.show.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private List<Long> artists = new ArrayList<>(); // 아티스트는 여러명일 가능성
    private Set<Long> crawlingGenres = new HashSet<>();
    private List<String> genres = new ArrayList<>(); // 공연의 장르는 한가지로 결정.
}
