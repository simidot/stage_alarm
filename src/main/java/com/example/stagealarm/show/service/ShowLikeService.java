package com.example.stagealarm.show.service;

import com.example.stagealarm.facade.AuthenticationFacade;
import com.example.stagealarm.show.dto.ShowLikeResponseDto;
import com.example.stagealarm.show.entity.ShowInfo;
import com.example.stagealarm.show.entity.ShowLike;
import com.example.stagealarm.show.repo.ShowInfoRepository;
import com.example.stagealarm.show.repo.ShowLikeRepository;
import com.example.stagealarm.user.entity.UserEntity;
import com.example.stagealarm.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class ShowLikeService {
    private final ShowLikeRepository showLikesRepository;
    private final UserRepository userRepository;
    private final ShowInfoRepository showInfoRepository;
    private final AuthenticationFacade facade;

    public ShowLikeResponseDto insertShowLike(Long showInfoId) {
        Long userId = facade.getUserEntity().getId();
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        ShowInfo showInfo = showInfoRepository.findById(showInfoId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 이미 좋아요가 되어있다면 예외처리
        if (showLikesRepository.findByUserEntityAndShowInfo(userEntity, showInfo).isPresent()) {
            new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        ShowLike like = ShowLike.builder()
                .userEntity(userEntity)
                .build();

        showInfo.addLike(like);

        showLikesRepository.save(like);


        ShowLikeResponseDto dto = ShowLikeResponseDto.builder()
                .userId(userId)
                .showInfoId(showInfoId)
                .likes((long) showInfo.getShowLikes().size())
                .build();

        return dto;
    }

    public void deleteShowLike(Long showInfoId) {
        Long userId = facade.getUserEntity().getId();
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        ShowInfo showInfo = showInfoRepository.findById(showInfoId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        ShowLike showLike = showLikesRepository.findByUserEntityAndShowInfo(userEntity, showInfo).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        showLikesRepository.delete(showLike);
    }

    public boolean isLiked(Long showInfoId, Long userId) {
        ShowLike byShowInfoIdAndUserId = showLikesRepository.findByShowInfoIdAndUserId(showInfoId, userId);
        boolean isLiked = byShowInfoIdAndUserId != null;
        return isLiked;
    }
}
