package com.example.stagealarm.show.service;

import com.example.stagealarm.show.dto.ShowInfoDto;
import com.example.stagealarm.show.entity.ShowInfo;
import com.example.stagealarm.show.repo.ShowInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ShowInfoService {
    private final ShowInfoRepository showInfoRepository;

    // 공연 정보 등록
    @Transactional
    public ResponseEntity<Void> create(ShowInfoDto dto, MultipartFile file) {
        // 관리자 계정인지 아닌지 확인하는 과정 추가로 필요
        ShowInfo showInfo = ShowInfo.builder()
                .date(dto.getDate())
                .startTime(dto.getStartTime())
                .hours(dto.getHours())
                .duration(dto.getDuration())
                .location(dto.getLocation())
                .title(dto.getTitle())
                .ticketVendor(dto.getTicketVendor())
                // s3service
                .posterImage("image")
                .price(dto.getPrice())
                .build();

        showInfoRepository.save(showInfo);
        return ResponseEntity.ok().build();
    }

    // 공연 정보 조회 (전체)
    public List<ShowInfo> readAll() {
        return showInfoRepository.findAll();
    }

    // 공연 정보 조히 (단일)
    public ShowInfo readOne(Long id) {
        return showInfoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }



    // 공연 정보 업데이트
    @Transactional
    public ResponseEntity<Void> update(Long id, ShowInfoDto dto) {
        // 관리자 계정인지 아닌지 확인하는 과정 추가로 필요

        ShowInfo showInfo = showInfoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        showInfo.setDate(dto.getDate());
        showInfo.setStartTime(dto.getStartTime());
        showInfo.setHours(dto.getHours());
        showInfo.setDuration(dto.getDuration());
        showInfo.setLocation(dto.getLocation());
        showInfo.setTitle(dto.getTitle());
        showInfo.setTicketVendor(dto.getTicketVendor());
        // s3service
        showInfo.setPosterImage("image");
        showInfo.setPrice(dto.getPrice());

        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<Void> delete(Long id) {
        showInfoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
