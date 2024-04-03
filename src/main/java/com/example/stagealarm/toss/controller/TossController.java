package com.example.stagealarm.toss.controller;

import com.example.stagealarm.order.service.OrderService;
import com.example.stagealarm.toss.dto.PaymentConfirmDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/toss")
@RequiredArgsConstructor
public class TossController {
    private final OrderService service;
    @PostMapping("/confirm-payment")
    public Object confirmPayment(
            @RequestBody
            PaymentConfirmDto dto
    ){
        log.info("received: {}", dto.toString());
        return service.confirmPayment(dto);
    }


}
