package com.example.stagealarm.toss.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentConfirmDto {
    private String paymentKey;
    private String orderId;
    private Integer amount;
}
