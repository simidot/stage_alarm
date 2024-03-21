package com.example.stagealarm.show.service;

import com.example.stagealarm.show.dto.ShowCommentsRequestDto;
import com.example.stagealarm.show.dto.ShowCommentsResponseDto;
import com.example.stagealarm.show.dto.ShowCommentsUpdateDto;
import com.example.stagealarm.show.entity.ShowComments;
import com.example.stagealarm.show.entity.ShowInfo;
import com.example.stagealarm.show.repo.ShowCommentsRepository;
import com.example.stagealarm.show.repo.ShowInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ShowCommentsService {
    private final ShowCommentsRepository showCommentsRepository;
    private final ShowInfoRepository showInfoRepository;

    // 댓글 작성
    public ShowCommentsResponseDto write(Long showInfoId, ShowCommentsRequestDto dto) {
        // 어떤 공연정보인지 확인
        ShowInfo showInfo = showInfoRepository.findById(showInfoId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        ShowComments showComments = ShowComments.builder()
                .content(dto.getContent())
                // 작성자가 누구인지 확인하는 로직 추가 필요
                // .userEntity()
                .showInfo(showInfo)
                .build();

        ShowComments save = showCommentsRepository.save(showComments);
        return ShowCommentsResponseDto.fromEntity(save);

    }

    // 댓글 보기
    @Transactional(readOnly = true)
    public List<ShowCommentsResponseDto> readAll(Long showInfoId) {
        List<ShowComments> byShowInfoId = showCommentsRepository.findByShowInfoId(showInfoId);
        return byShowInfoId.stream().map(ShowCommentsResponseDto::fromEntity).collect(Collectors.toList());
    }

    // 댓글 업데이트(내용만 수정 가능)
    public void update(Long id, ShowCommentsUpdateDto dto) {
        ShowComments showComments = showCommentsRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        showComments.setContent(dto.getContent());
    }

    // 댓글 삭제(삭제 전략 미정)
    public void delete(Long id) {
        showCommentsRepository.deleteById(id);
    }
}
