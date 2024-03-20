package com.example.stagealarm.board.entity;

import lombok.Getter;

@Getter
public enum ActivateEnum {
  ACTIVATE("활성화"),
  INACTIVATE("비활성화");

  private String activate;

  ActivateEnum(String activate) {
    this.activate = activate;
  }
}
