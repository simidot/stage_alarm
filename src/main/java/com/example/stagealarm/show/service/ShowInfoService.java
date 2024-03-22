package com.example.stagealarm.show.service;

import com.example.stagealarm.awsS3.S3FileService;
import com.example.stagealarm.show.dto.ShowInfoRequestDto;
import com.example.stagealarm.show.dto.ShowInfoResponseDto;
import com.example.stagealarm.show.dto.Sortable;
import com.example.stagealarm.show.entity.ShowInfo;
import com.example.stagealarm.show.repo.QShowInfoRepository;
import com.example.stagealarm.show.repo.ShowInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    private final QShowInfoRepository qShowInfoRepository;
    private final S3FileService s3FileService;

    // 공연 정보 등록
    @Transactional
    public ShowInfoResponseDto create(ShowInfoRequestDto dto, MultipartFile file) {
        List<String> s3List = s3FileService.uploadIntoS3("/showInfoImg", List.of(file));

        ShowInfo showInfo = ShowInfo.builder()
                .date(dto.getDate())
                .startTime(dto.getStartTime())
                .hours(dto.getHours())
                .duration(dto.getDuration())
                .location(dto.getLocation())
                .title(dto.getTitle())
                .ticketVendor(dto.getTicketVendor())
                // s3service
                .posterImage(s3List.get(0))
                .price(dto.getPrice())
                .build();

        ShowInfo saved = showInfoRepository.save(showInfo);

        return ShowInfoResponseDto.fromEntity(saved);
    }

    // 공연 정보 조회 (전체)
    public Page<ShowInfoResponseDto> readAll(String title, Pageable pageable, Sortable sortable) {
        Page<ShowInfo> all = qShowInfoRepository.findAll(title, pageable, sortable);

        return all.map(ShowInfoResponseDto::fromEntity);
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
