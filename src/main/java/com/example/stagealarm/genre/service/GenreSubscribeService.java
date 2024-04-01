package com.example.stagealarm.genre.service;

import com.example.stagealarm.facade.AuthenticationFacade;
import com.example.stagealarm.genre.dto.GenreSubscribeDto;
import com.example.stagealarm.genre.entity.Genre;
import com.example.stagealarm.genre.entity.GenreSubscribe;
import com.example.stagealarm.genre.repo.GenreRepository;
import com.example.stagealarm.genre.repo.GenreSubscribeRepo;
import com.example.stagealarm.user.entity.UserEntity;
import com.example.stagealarm.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreSubscribeService {
    private final GenreSubscribeRepo subscribeRepo;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final AuthenticationFacade facade;


    @Transactional
    public GenreSubscribeDto subscribeGenre(Long genreId) {
        //로그인한 계정 아이디로 유저 찾기
        Long userId = facade.getUserEntity().getId();
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "genre not found"));

        GenreSubscribeDto resultDto = new GenreSubscribeDto();
        // 구독이 되어있을 시에는 구독 취소 (해당 데이터 삭제)
        if (userEntity.getGenreSubscribeList().stream()
                .anyMatch(subscribe -> subscribe.getGenre().equals(genre))) {
            GenreSubscribe genreSubscribe = subscribeRepo.findByUserEntityAndGenre(userEntity, genre)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "genre-subscribe not found"));
            subscribeRepo.delete(genreSubscribe);
            genre.getSubscribes().remove(genreSubscribe);
        } else { // 구독 안되어있을 시에는 구독 객체 생성
            GenreSubscribe genreSubscribe = GenreSubscribe.builder()
                    .userEntity(userEntity)
                    .genre(genre)
                    .build();
            subscribeRepo.save(genreSubscribe);
            resultDto.setGenreId(genre.getId());
            resultDto.setUserId(userEntity.getId());
        }
        genreRepository.save(genre);
        resultDto.setSubscribes((long) genre.getSubscribes().size());
        return resultDto;
    }
}
