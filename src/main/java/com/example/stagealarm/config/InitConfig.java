package com.example.stagealarm.config;

import com.example.stagealarm.genre.entity.Genre;
import com.example.stagealarm.genre.repo.GenreRepository;
import com.example.stagealarm.show.entity.ShowInfo;
import com.example.stagealarm.show.repo.ShowInfoRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class InitConfig {
    private final ShowInfoRepository showInfoRepository;
    private final GenreRepository genreRepository;

    @PostConstruct
    public void createShowInfo() {
        for (int i = 1; i <= 10; i++) {
            ShowInfo showInfo = ShowInfo.builder()
                    .date(LocalDate.of(2024, 4, 1 + i))
                    .hours(120)
                    .posterImage("https://s3.ap-northeast-2.amazonaws.com/stage-alarm/boardImg/f23dc916-476f-46b4-841c-be9968509cc6.jpg")
                    .title("대프리카 축제 " + i) // 제목에 숫자를 추가하여 구분
                    .duration(3)
                    .location("대구")
                    .ticketVendor("주소")
                    .startTime(LocalTime.of(1, 0))
                    .price("20000원")
                    .build();
            showInfoRepository.save(showInfo);
        }
    }

    @PostConstruct
    public void createGenres() {
        List<Genre> genres = new ArrayList<>();

        // 공연 장르 생성
        genres.add(Genre.builder().name("아이돌").build());
        genres.add(Genre.builder().name("팬클럽/팬미팅").build());
        genres.add(Genre.builder().name("발라드/R&B").build());
        genres.add(Genre.builder().name("힙합/EDM").build());
        genres.add(Genre.builder().name("페스티벌").build());
        genres.add(Genre.builder().name("인디").build());
        genres.add(Genre.builder().name("락/메탈").build());
        genres.add(Genre.builder().name("포크/트로트").build());
        genres.add(Genre.builder().name("내한공연").build());
        genres.add(Genre.builder().name("팝").build());
        genres.add(Genre.builder().name("클래식").build());
        genres.add(Genre.builder().name("재즈").build());
        genres.add(Genre.builder().name("케이팝").build());
        genres.add(Genre.builder().name("댄스/일렉트로닉").build());
        genres.add(Genre.builder().name("그 외 장르").build());

        // 장르 저장
        genreRepository.saveAll(genres);
    }
}
