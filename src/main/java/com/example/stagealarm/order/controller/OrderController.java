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

    @GetMapping
    public List<ItemOrderDto> readAll(){
        return service.readAll();
    }

    @GetMapping("{id}")
    public ItemOrderDto readOne(
            @PathVariable("id")
            Long id
    ){
        return service.searchById(id);
    }


    @GetMapping("{id}/payment")
    public Object readTossPayment(
            @PathVariable("id")
            Long id
    ){
        return service.readTossPayment(id);
    }

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
