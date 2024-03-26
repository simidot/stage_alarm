package com.example.stagealarm.show.dto;

import com.example.stagealarm.show.entity.ShowInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor
public class ShowInfoResponseDto {
    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private Integer hours;
    private Integer duration;
    private String location;
    private String title;
    private String ticketVendor;
    private String price;
    private Long totalLike;

    public static ShowInfoResponseDto fromEntity(ShowInfo showInfo) {
        return ShowInfoResponseDto.builder()
                .id(showInfo.getId())
                .date(showInfo.getDate())
                .startTime(showInfo.getStartTime())
                .hours(showInfo.getHours())
                .duration(showInfo.getDuration())
                .location(showInfo.getLocation())
                .title(showInfo.getTitle())
                .ticketVendor(showInfo.getTicketVendor())
                .price(showInfo.getPrice())
                .build();
    }
}
