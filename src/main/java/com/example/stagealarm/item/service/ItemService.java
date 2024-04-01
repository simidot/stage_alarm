package com.example.stagealarm.item.service;

import com.example.stagealarm.awsS3.S3FileService;
import com.example.stagealarm.enums.Status;
import com.example.stagealarm.item.dto.ItemDto;
import com.example.stagealarm.item.entity.Item;
import com.example.stagealarm.item.repo.ItemRepository;
import com.example.stagealarm.show.repo.ShowInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;
    private final ShowInfoRepository showInfoRepository;
    private final S3FileService s3FileService;
    public ItemService(ItemRepository itemRepository,
                       ShowInfoRepository showInfoRepository,
                       S3FileService s3FileService) {
        this.itemRepository = itemRepository;
        this.showInfoRepository = showInfoRepository;
        this.s3FileService = s3FileService;

    }
    // 모든 아이템 보기(관리자)
    public List<ItemDto> readAll() {
        return itemRepository.findAll().stream()
                .map(ItemDto::fromEntity)
                .collect(Collectors.toList());
    }

    // id로 아이템 찾기
    public ItemDto searchById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        return ItemDto.fromEntity(item);
    }

    // 아이템 추가(관리자)
    @Transactional
    public ItemDto create(ItemDto itemDto, MultipartFile file) {
        Item item = Item.builder()
                .name(itemDto.getName())
                .content(itemDto.getContent())
                .price(itemDto.getPrice())
                .amount(itemDto.getAmount())
                .itemImg(s3FileService.uploadIntoS3("/profileImg", file))
                .status(Status.SALE)
                .build();
        // 공연정보
        item.setShowInfo(showInfoRepository.findById(itemDto.getShowInfoId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        ));
        return ItemDto.fromEntity(itemRepository.save(item));
    }

    // 아이템 업데이트
    @Transactional
    public ItemDto update(Long id, ItemDto itemDto) {
        Item item = itemRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        item.setItemImg(itemDto.getItemImg());
        item.setName(item.getName());
        item.setPrice(item.getPrice());
        item.setAmount(item.getAmount());
        item.setContent(item.getContent());
        item.setStatus(item.getStatus());

        return ItemDto.fromEntity(item);

    }

    // 아이템 삭제
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }


    public List<ItemDto> searchByShowInfoId(Long showInfoId) {
        return itemRepository.findAllByShowInfoId(showInfoId)
                .stream().map(ItemDto::fromEntity).collect(Collectors.toList());
    }
}


