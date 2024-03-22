package com.example.stagealarm.show.entity;

import com.example.stagealarm.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ShowInfo extends BaseEntity {

  @Column(nullable = false)
  @Setter
  private LocalDate date; //공연 날짜
  @Setter
  private LocalTime startTime; //공연 시작 시간
  @Setter
  private Integer hours; //예상 공연 시간
  @Setter
  private Integer duration; //공연 진행 기간 (3일이면 72시간)

  @Column(nullable = false)
  @Setter
  private String location;
  @Column(nullable = false)
  @Setter
  private String title;
  @Setter
  private String ticketVendor; //예매 사이트 url
  @Setter
  private String posterImage;
  @Column(nullable = false)
  @Setter
  private String price;

  @OneToMany(mappedBy = "showInfo")
  private List<ShowLike> showLikes;
}
