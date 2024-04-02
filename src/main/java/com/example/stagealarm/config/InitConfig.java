package com.example.stagealarm.config;

import com.example.stagealarm.show.entity.ShowInfo;
import com.example.stagealarm.show.repo.ShowInfoRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalTime;

@Configuration
@RequiredArgsConstructor
public class InitConfig {
    private final ShowInfoRepository showInfoRepository;

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
}
