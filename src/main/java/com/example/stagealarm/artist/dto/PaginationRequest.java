package com.example.stagealarm.artist.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {
  private int page;
  private int size;
}
