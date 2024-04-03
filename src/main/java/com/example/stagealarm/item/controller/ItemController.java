package com.example.stagealarm.item.controller;

import com.example.stagealarm.item.dto.ItemDto;
import com.example.stagealarm.item.service.ItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Item 컨트롤러", description = "Item API입니다.")
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

    @PatchMapping("items")
    public ItemDto update(
            @RequestPart("dto") ItemDto dto,
            @RequestPart(name = "file") MultipartFile file
    ){
        return itemService.update(dto, file);
    }

    @DeleteMapping("items/{id}")
    public void delete(
            @PathVariable("id")
            Long id
    ){
        itemService.delete(id);
    }
}
