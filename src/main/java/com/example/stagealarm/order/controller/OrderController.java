package com.example.stagealarm.order.controller;

import com.example.stagealarm.order.dto.ItemOrderDto;
import com.example.stagealarm.order.service.OrderService;
import com.example.stagealarm.toss.dto.PaymentCancelDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order 컨트롤러", description = "Order API입니다.")
@RestController
@RequestMapping
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    // 모든 주문 보기
    @GetMapping("/orders")
    public List<ItemOrderDto> readAll(){
        return service.readAll();
    }

    // 아이디로 주문 찾기
    @GetMapping("/orders/{id}")
    public ItemOrderDto readOne(
            @PathVariable("id")
            Long id
    ){
        return service.searchById(id);
    }

    // 내 주문 찾기
    @GetMapping("myOrder")
    public List<ItemOrderDto> readAllByUser(
    ){
        return service.searAllByUserId();
    }


    @GetMapping("/orders/{id}/payment")
    public Object readTossPayment(
            @PathVariable("id")
            Long id
    ){
        return service.readTossPayment(id);
    }

    // 주문취소 하기
    @PostMapping("/orders/{id}/cancel")
    public Object cancelPayment(
            @PathVariable("id")
            Long id,
            @RequestBody
            PaymentCancelDto dto
    ){
        return service.cancelPayment(id, dto);
    }
}
