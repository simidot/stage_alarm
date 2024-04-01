package com.example.stagealarm.item.controller;

import com.example.stagealarm.item.dto.ItemDto;
import com.example.stagealarm.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    @GetMapping("items")
    public List<ItemDto> readAll() {
        log.info("readAll");
        return itemService.readAll();
    }

    @GetMapping("{showInfoId}/items")
    public List<ItemDto> readAllByShowInfo(
            @PathVariable("showInfoId")
            Long showInfoId
    ){
        return itemService.searchByShowInfoId(showInfoId);
    }

    @GetMapping("items/{id}")
    public ItemDto readOne(
            @PathVariable("id")
            Long id
    ) {
        return itemService.searchById(id);
    }

    @PostMapping("items")
    public ItemDto create(
            @RequestPart("dto") ItemDto dto,
            @RequestPart(name = "file") MultipartFile file
    ){
        return itemService.create(dto, file);
    }
}
