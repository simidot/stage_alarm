package com.example.stagealarm.show;

import com.example.stagealarm.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ShowInfo extends BaseEntity {

  @Column(nullable = false)
  private LocalDate date; //공연 날짜
  private LocalDateTime startTime; //공연 시작 시간
  private Integer hours; //예상 공연 시간
  private Integer duration; //공연 진행 기간 (3일이면 72시간)

  @Column(nullable = false)
  private String location;
  @Column(nullable = false)
  private String title;
  private String ticketVendor; //예매 사이트 url
  private String posterImage;
  @Column(nullable = false)
  private String price;
}
