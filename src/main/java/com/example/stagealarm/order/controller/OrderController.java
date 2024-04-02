package com.example.stagealarm.order.controller;

import com.example.stagealarm.order.dto.ItemOrderDto;
import com.example.stagealarm.order.service.OrderService;
import com.example.stagealarm.toss.dto.PaymentCancelDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    // 모든 주문 보기
    @GetMapping
    public List<ItemOrderDto> readAll(){
        return service.readAll();
    }

    // 아이디로 주문 찾기
    @GetMapping("{id}")
    public ItemOrderDto readOne(
            @PathVariable("id")
            Long id
    ){
        return service.searchById(id);
    }

    // 내 주문 찾기
    @GetMapping("my")
    public List<ItemOrderDto> readAllByUser(
    ){
        return service.searAllByUserId();
    }


    @GetMapping("{id}/payment")
    public Object readTossPayment(
            @PathVariable("id")
            Long id
    ){
        return service.readTossPayment(id);
    }

    // 주문취소 하기
    @PostMapping("{id}/cancel")
    public Object cancelPayment(
            @PathVariable("id")
            Long id,
            @RequestBody
            PaymentCancelDto dto
    ){
        return service.cancelPayment(id, dto);
    }
}
