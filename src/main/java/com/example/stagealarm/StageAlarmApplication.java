package com.example.stagealarm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync(proxyTargetClass = true)
public class StageAlarmApplication {

    public static void main(String[] args) {
        SpringApplication.run(StageAlarmApplication.class, args);
    }

}
