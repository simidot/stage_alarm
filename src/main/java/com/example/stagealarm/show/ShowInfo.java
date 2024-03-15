package com.example.stagealarm.show;

import com.example.stagealarm.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ShowInfo extends BaseEntity {
  private LocalDate time;
  private LocalDate duration;
  private String location;
  private String title;
  private String ticketVendor;
  private String posterImages;
  private String price; // Integer이어야 하지 않을까요..?
}
