package com.example.stagealarm.show.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

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

    private Long artistId; // 아티스트도 어떻게 받아와야할지?
    private Long genreId; //프론트 입력창에서 장르를 선택 가능하도록..?하고 받아옴
}
