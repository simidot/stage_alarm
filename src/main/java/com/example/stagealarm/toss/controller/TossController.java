package com.example.stagealarm.toss.controller;

import com.example.stagealarm.order.service.OrderService;
import com.example.stagealarm.toss.dto.PaymentConfirmDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Toss 컨트롤러", description = "Toss API입니다.")
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
