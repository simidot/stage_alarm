package com.example.stagealarm.show.dto;

import com.querydsl.core.types.Expression;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(staticName = "of")
@Getter
public class SortBinder {

  private String name;
  private Expression target;

}
