package com.example.stagealarm.config;

import com.example.stagealarm.show.entity.ShowInfo;
import com.example.stagealarm.show.repo.ShowInfoRepository;
import com.example.stagealarm.user.entity.UserEntity;
import com.example.stagealarm.user.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

//@Component
public class TestDataBean {

    public TestDataBean(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            ShowInfoRepository showInfoRepository
    ) {

        userRepository.save(UserEntity.builder()
                .loginId("admin")
                .password(passwordEncoder.encode("password"))
                .email("ggg@gmail.com")
                .authorities("ROLE_ADMIN")
                .build());


        userRepository.save(UserEntity.builder()
                .loginId("user")
                .password(passwordEncoder.encode("password"))
                .email("hhhjs0133@naver.com")
                .nickname("일반 사용자1")
                .authorities("ROLE_USER")
                .build());

        // ShowInfo 인스턴스 생성
        ShowInfo testShowInfo = ShowInfo.builder()
                .date(LocalDate.of(2024, 4, 1)) // 공연 날짜
                .startTime(LocalTime.of(19, 30)) // 공연 시작 시간
                .hours(2) // 예상 공연 시간(시간 단위)
                .duration(48) // 공연 진행 기간(시간 단위)
                .location("서울 예술의전당") // 공연 장소
                .title("봄바람 콘서트") // 공연 제목
                .ticketVendor("http://ticket.example.com") // 예매 사이트 URL
                .posterImage("http://example.com/poster.jpg") // 포스터 이미지 URL
                .price("전석 50,000원") // 가격
                .build();

        // 데이터베이스에 저장
        showInfoRepository.save(testShowInfo);

    }
}
