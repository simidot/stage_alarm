package com.example.stagealarm.show.service;

import com.example.stagealarm.show.dto.ShowInfoRequestDto;
import com.example.stagealarm.show.dto.ShowInfoResponseDto;
import com.example.stagealarm.show.entity.ShowInfo;
import com.example.stagealarm.show.repo.ShowInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ShowInfoService {
    private final ShowInfoRepository showInfoRepository;

    // 공연 정보 등록
    @Transactional
    public ShowInfoResponseDto create(ShowInfoRequestDto dto, MultipartFile file) {
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

        ShowInfo saved = showInfoRepository.save(showInfo);

        return ShowInfoResponseDto.fromEntity(saved);
    }

    // 공연 정보 조회 (전체)
    public List<ShowInfoResponseDto> readAll() {
        List<ShowInfo> all = showInfoRepository.findAll();

        return all.stream().map(ShowInfoResponseDto::fromEntity).collect(Collectors.toList());
    }

    // 공연 정보 조히 (단일)
    public ShowInfoResponseDto readOne(Long id) {
        ShowInfo showInfo = showInfoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ShowInfoResponseDto.fromEntity(showInfo);
    }



    // 공연 정보 업데이트
    @Transactional
    public void update(Long id, ShowInfoRequestDto dto) {
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

    }

    @Transactional
    public void delete(Long id) {
        showInfoRepository.deleteById(id);
    }

}
