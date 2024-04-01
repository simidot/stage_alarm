package com.example.stagealarm.show.dto;

import com.example.stagealarm.artist.dto.ArtistDto;
import com.example.stagealarm.genre.dto.GenreDto;
import com.example.stagealarm.show.entity.ShowArtist;
import com.example.stagealarm.show.entity.ShowGenre;
import com.example.stagealarm.show.entity.ShowInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
    @Setter
    private Boolean isLiked;

    private List<ShowArtist> artists;
    private List<ShowGenre> genres;

    public ShowInfoResponseDto(Long id, LocalDate date, LocalTime startTime, Integer hours, Integer duration,
                               String location, String title, String ticketVendor, String price, Long totalLike) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.hours = hours;
        this.duration = duration;
        this.location = location;
        this.title = title;
        this.ticketVendor = ticketVendor;
        this.price = price;
        this.totalLike = totalLike;
    }

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
            .genres(showInfo.getShowGenres())
            .artists(showInfo.getShowArtists())
            .price(showInfo.getPrice())
            .build();
    }
}
