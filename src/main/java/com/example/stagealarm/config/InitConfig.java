package com.example.stagealarm.config;

import com.example.stagealarm.genre.entity.Genre;
import com.example.stagealarm.genre.repo.GenreRepository;
import com.example.stagealarm.user.entity.UserEntity;
import com.example.stagealarm.user.repo.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class InitConfig {
    private final GenreRepository genreRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @PostConstruct
    public void createGenres() {
        if(genreRepository.count() == 0) {
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

            userRepository.save(UserEntity.builder()
                    .loginId("admin")
                    .password(passwordEncoder.encode("password"))
                    .email("ggg@gmail.com")
                    .authorities("ROLE_ADMIN")
                    .build());


            userRepository.save(UserEntity.builder()
                    .loginId("user")
                    .password(passwordEncoder.encode("123"))
                    .email("hhhjs0133@naver.com")
                    .nickname("천재")
                    .authorities("ROLE_USER")
                    .build());

            userRepository.save(UserEntity.builder()
                    .loginId("lks9909")
                    .password(passwordEncoder.encode("123"))
                    .email("hhhjs0133@gmail.com")
                    .nickname("강선마을")
                    .authorities("ROLE_USER")
                    .build());

            userRepository.save(UserEntity.builder()
                    .loginId("abc")
                    .password(passwordEncoder.encode("123"))
                    .email("hhjs0133@gmail.com")
                    .nickname("강마에")
                    .authorities("ROLE_USER")
                    .build());
        }

    }
}
