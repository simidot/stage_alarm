package com.example.stagealarm.item.controller;

import com.example.stagealarm.artist.dto.PaginationRequest;
import com.example.stagealarm.item.dto.ItemDto;
import com.example.stagealarm.item.service.ItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public Page<ItemDto> readAll(@PageableDefault(size = 5) PaginationRequest paginationRequest) {
        Pageable pageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize());

        return itemService.readAll(pageable);
    }

    @GetMapping("{showInfoId}/items")
    public Page<ItemDto> readAllByShowInfo(
        @PathVariable("showInfoId")
        Long showInfoId,
        @PageableDefault(size = 5) PaginationRequest paginationRequest
    ) {
        Pageable pageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize());
        return itemService.searchByShowInfoId(showInfoId, pageable);
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
