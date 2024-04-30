package com.example.stagealarm.item.service;

import com.example.stagealarm.awsS3.S3FileService;
import com.example.stagealarm.item.entity.Status;
import com.example.stagealarm.item.dto.ItemDto;
import com.example.stagealarm.item.entity.Item;
import com.example.stagealarm.item.repo.ItemRepository;
import com.example.stagealarm.item.repo.QItemRepo;
import com.example.stagealarm.show.repo.ShowInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;
    private final ShowInfoRepository showInfoRepository;
    private final S3FileService s3FileService;
    private final QItemRepo qItemRepo;

    // 모든 아이템 보기(관리자)
    public Page<ItemDto> readAll(Pageable pageable) {
        Page<Item> itemPage = qItemRepo.findAll(pageable);

        return itemPage.map(ItemDto::fromEntity);
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
                .itemImg(s3FileService.uploadIntoS3("/itemImg", file))
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
    public ItemDto update(ItemDto itemDto , MultipartFile file) {
        Item item = itemRepository.findById(itemDto.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        item.setItemImg(s3FileService.uploadIntoS3("/itemImg", file));
        item.setName(item.getName());
        item.setPrice(item.getPrice());
        item.setAmount(item.getAmount());
        item.setContent(item.getContent());
        item.setStatus(Status.SALE);

        return ItemDto.fromEntity(item);

    }

    // 아이템 업데이트(주문시 수량 및 상태)
    @Transactional
    public ItemDto updateAmount(Long id, Integer quantity) {
        Item item = itemRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        if(item.getAmount() + quantity < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수량이 재고보다 많습니다.");
        }

        // quantity 가 음수면 주문, 양수면 주문 취소
        item.setAmount(item.getAmount() + quantity);

        if(item.getAmount() == 0){
            item.setStatus(Status.SOLD);
        } else{
            item.setStatus(Status.SALE);

        }

        return ItemDto.fromEntity(item);
    }

    // 아이템 삭제
    @Transactional
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }


    public Page<ItemDto> searchByShowInfoId(Long showInfoId, Pageable pageable) {
        Page<Item> itemPage = qItemRepo.findAllByShowInfoId(showInfoId, pageable);
        return itemPage.map(ItemDto::fromEntity);
    }
}


