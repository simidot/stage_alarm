package com.example.stagealarm.genre.service;

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

    @Transactional
    public void subscribeGenre(GenreSubscribeDto dto) {
        UserEntity userEntity = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        Genre genre = genreRepository.findById(dto.getGenreId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "genre not found"));

        //이미 구독 되어 있으면 에러 반환
        if (subscribeRepo.findByUserEntityAndGenre(userEntity, genre).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user already subscribed this genre");
        }

        GenreSubscribe subscribe = GenreSubscribe.builder()
                .genre(genre)
                .userEntity(userEntity)
                .build();
        subscribeRepo.save(subscribe);
    }
}
