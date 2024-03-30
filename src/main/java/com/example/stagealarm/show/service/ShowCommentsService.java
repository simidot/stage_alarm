package com.example.stagealarm.show.service;

import com.example.stagealarm.facade.AuthenticationFacade;
import com.example.stagealarm.show.dto.ShowCommentsRequestDto;
import com.example.stagealarm.show.dto.ShowCommentsResponseDto;
import com.example.stagealarm.show.dto.ShowCommentsUpdateDto;
import com.example.stagealarm.show.entity.ShowComments;
import com.example.stagealarm.show.entity.ShowInfo;
import com.example.stagealarm.show.repo.ShowCommentsRepository;
import com.example.stagealarm.show.repo.ShowInfoRepository;
import com.example.stagealarm.user.entity.UserEntity;
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
    private final AuthenticationFacade facade;

    // 댓글 작성
    public ShowCommentsResponseDto write(Long showInfoId, ShowCommentsRequestDto dto) {
        // 어떤 공연정보인지 확인
        ShowInfo showInfo = showInfoRepository.findById(showInfoId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        UserEntity userEntity = facade.getUserEntity();

        ShowComments showComments = ShowComments.builder()
                .content(dto.getContent())
                // 작성자가 누구인지 확인하는 로직 추가 필요
                .userEntity(userEntity)
                .showInfo(showInfo)
                .build();
        Long id = userEntity == null ? null : userEntity.getId();
        ShowComments save = showCommentsRepository.save(showComments);
        return ShowCommentsResponseDto.fromEntity(save, id);

    }

    // 댓글 보기
    @Transactional(readOnly = true)
    public List<ShowCommentsResponseDto> readAll(Long showInfoId) {
        UserEntity userEntity = facade.getUserEntity();
        Long id = userEntity == null ? null : userEntity.getId();

        List<ShowComments> byShowInfoId = showCommentsRepository.findByShowInfoId(showInfoId);
        return byShowInfoId.stream().map(comment -> ShowCommentsResponseDto.fromEntity(comment, id)).collect(Collectors.toList());
    }

    // 댓글 업데이트(내용만 수정 가능)
    public void update(Long id, ShowCommentsUpdateDto dto) {
        ShowComments showComments = showCommentsRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        UserEntity userEntity = facade.getUserEntity();

        // 로그인 유저와 댓글작성한 유저가 일치하지 않으면 예외처리
        if (!userEntity.equals(showComments.getUserEntity())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        showComments.setContent(dto.getContent());
    }

    // 댓글 삭제(db에서도 삭제)
    public void delete(Long id) {
        ShowComments showComments = showCommentsRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        UserEntity userEntity = facade.getUserEntity();

        // 로그인 유저와 댓글작성한 유저가 일치하지 않으면 예외처리
        if (!userEntity.equals(showComments.getUserEntity())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        showCommentsRepository.deleteById(id);
    }
}
