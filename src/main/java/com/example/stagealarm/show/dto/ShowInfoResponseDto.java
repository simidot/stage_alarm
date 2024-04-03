package com.example.stagealarm.show.dto;

import com.example.stagealarm.artist.dto.ArtistDto;
import com.example.stagealarm.genre.dto.GenreDto;
import com.example.stagealarm.show.entity.ShowInfo;
import com.example.stagealarm.show.entity.ShowLike;
import com.example.stagealarm.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

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
    private String posterImage;
    private Long totalLike;
    @Setter
    private Boolean isLiked;

    private List<ArtistDto> artists;
    private List<GenreDto> genres;

    public ShowInfoResponseDto(Long id, LocalDate date, LocalTime startTime, Integer hours, Integer duration,
                               String location, String title, String ticketVendor, String price, String posterImage, Long totalLike) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.hours = hours;
        this.duration = duration;
        this.location = location;
        this.title = title;
        this.ticketVendor = ticketVendor;
        this.price = price;
        this.posterImage = posterImage;
        this.totalLike = totalLike;
    }

    public static ShowInfoResponseDto fromEntity(ShowInfo showInfo, UserEntity user) {
        boolean isLiked = false;
        Long userId = user == null ? null : user.getId();
        if (showInfo.getShowLikes() != null) {
            for (ShowLike showLike : showInfo.getShowLikes()) {
                if (Objects.equals(showLike.getUserEntity().getId(), userId)) {
                    isLiked = true;
                    break;
                }
            }
        }

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
                .posterImage(showInfo.getPosterImage())
                .totalLike((long) showInfo.getShowLikes().size())
                .isLiked(isLiked)
                .build();
    }
}
